package com.hokage.ssh;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hokage.biz.service.HokageServerService;
import com.hokage.common.ServiceResponse;
import com.hokage.infra.worker.ThreadPoolWorker;
import com.hokage.persistence.dataobject.HokageServerDO;
import com.hokage.ssh.enums.JSchChannelType;
import com.hokage.websocket.WebSocketAndSshSession;
import com.hokage.websocket.WebSocketMessage;
import com.hokage.websocket.enums.WebSocketMessageType;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author yiminlin
 */

@Slf4j
@Component
public class SshComponent {

    private HokageServerService serverService;

    @Autowired
    public void setServerService(HokageServerService serverService) {
        this.serverService = serverService;
    }

    private ThreadPoolWorker worker;

    @Autowired
    public void setPool(ThreadPoolWorker worker) {
        this.worker = worker;
    }

    private static final Map<String, WebSocketAndSshSession> WEB_SOCKET_SESSIONS = new ConcurrentHashMap<>();


    /**
     * @param session ws session
     */
    public void add(WebSocketSession session) {

        JSch jSch = new JSch();

        WebSocketAndSshSession webSocketAndSshSession = new WebSocketAndSshSession();
        webSocketAndSshSession.setWebSocketSession(session);
        webSocketAndSshSession.setJSch(jSch);

        WEB_SOCKET_SESSIONS.put(session.getId(), webSocketAndSshSession);

    }

    /**
     * 处理来自xterm发来的字符
     * @param message message content
     * @param session ws session
     */
    public void handleMsgFromXterm(WebSocketMessage<String> message, WebSocketSession session) {
        try {
            // xterm发起连接请求信息
            if (StringUtils.equals(message.getType(), WebSocketMessageType.XTERM_SSH_INIT.getValue())) {

                SshConnectionInfo connectionInfo = JSON.parseObject(message.getData(), new TypeReference<SshConnectionInfo>(){});

                // retrieve ssh password
                ServiceResponse<List<HokageServerDO>> response = serverService.selectByIds(Collections.singletonList(connectionInfo.getId()));
                if (!response.getSucceeded() || CollectionUtils.isEmpty(response.getData())) {
                    throw new RuntimeException("server is not exist. connection information: " + JSON.toJSONString(connectionInfo));
                }

                String password = response.getData().get(0).getPasswd();
                connectionInfo.setPasswd(password);

                WebSocketAndSshSession webSocketDO = WEB_SOCKET_SESSIONS.get(session.getId());

                if (Objects.nonNull(webSocketDO)) {
                    // 丢给线程进行处理
                    this.worker.getExecutorPool().execute(() -> {
                        try {
                            connectToSsh(webSocketDO, connectionInfo, session);
                        } catch (Exception e) {
                            log.error("ssh连接出错", e);
                        }
                    });
                }

            }

            // 处理xterm输入的字符
            if (StringUtils.equals(message.getType(), WebSocketMessageType.XTERM_SSH_DATA.getValue())) {
                WebSocketAndSshSession webSocketDO = WEB_SOCKET_SESSIONS.get(session.getId());
                if (Objects.nonNull(webSocketDO)) {
                    sendToSsh(webSocketDO.getChannel(), message.getData());
                }
            }
        } catch (Exception e) {
            log.error("handle message exception", e);
            this.close(session);
        }
    }

    /**
     * ssh连接,并启动一个线程,监听ssh返回的数据,并发送到WebSocket客户端,x-term进行展示
     * @param webSocketAndSshSession socket and ssh session
     * @param connectionInfo ssh connection information
     * @param session websocket session
     */
    public void connectToSsh(WebSocketAndSshSession webSocketAndSshSession, SshConnectionInfo connectionInfo, WebSocketSession session) {

        Properties config = new Properties();
        // jsch的特性,如果没有这个配置,无法完成ssh连接
        config.put("StrictHostKeyChecking", "no");

        Session jSchSession = null;

        try {

            JSch jSch = webSocketAndSshSession.getJSch();
            jSchSession = jSch.getSession(connectionInfo.getAccount(), connectionInfo.getIp(), Integer.parseInt(connectionInfo.getSshPort()));

            jSchSession.setConfig(config);
            jSchSession.setPassword(connectionInfo.getPasswd());
            jSchSession.connect(30 * 1000);

            Channel channel = jSchSession.openChannel(JSchChannelType.SHELL.getValue());
            channel.connect(30 * 1000);

            webSocketAndSshSession.setChannel(channel);

            // 读取shell的数据
            InputStream input = channel.getInputStream();
            byte[] buf = new byte[32 * 1024];

            while (true) {
                int length = input.read(buf);
                if (length < 0) {
                    sendToWebSocket(session, "\r\nssh链接已断开".getBytes(StandardCharsets.UTF_8));
                    break;
                }
                sendToWebSocket(session, Arrays.copyOfRange(buf, 0, length));
            }

        } catch (Exception e) {
            log.error("connect to ssh error", e);
        } finally {
            if (Objects.nonNull(jSchSession)) {
                jSchSession.disconnect();
            }
        }
    }

    /**
     * 将前端通过websocket传过来的数据通过ssh转给服务器
     * @param channel jsch channel
     * @param data message from xterm
     * @throws IOException exception
     */
    public void sendToSsh(Channel channel, String data) throws IOException {
        if (Objects.nonNull(channel)) {
            OutputStream output = channel.getOutputStream();
            output.write(data.getBytes());
            output.flush();

        }
    }

    /**
     * 将服务通过ssh发送过来的信息转给WebSocket,由x-term进行展示
     * @param session websocket session
     * @param buffer message bytes from server
     * @throws IOException exception
     */
    public void sendToWebSocket(WebSocketSession session, byte[] buffer) throws IOException {
        session.sendMessage(new TextMessage(buffer));
    }

    /**
     * 关闭WebSocket连接,并关闭ssh连接
     * @param session websocket session
     */
    public void close(WebSocketSession session) {
        WebSocketAndSshSession webSocketDO = WEB_SOCKET_SESSIONS.get(session.getId());
        if (Objects.nonNull(webSocketDO)) {
            if (Objects.nonNull(webSocketDO.getChannel())) {
                webSocketDO.getChannel().disconnect();
            }
            WEB_SOCKET_SESSIONS.remove(session.getId());
        }
    }

}
