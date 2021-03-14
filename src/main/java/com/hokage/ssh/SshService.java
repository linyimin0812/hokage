package com.hokage.ssh;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hokage.ssh.enums.JSchChannelType;
import com.hokage.websocket.WebSocketDO;
import com.hokage.websocket.WebSocketMessage;
import com.hokage.websocket.enums.WebSocketMessageType;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class SshService {

    private SshConnectionInfo connectionInfo;
    private static final Map<String, WebSocketDO> webSocketSessions = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * 前端打开一个ssh连接页面时, 初始化一个ssh连接
     * @param session
     */
    public void add(WebSocketSession session) {

        JSch jSch = new JSch();

        WebSocketDO webSocketDO = new WebSocketDO();
        webSocketDO.setWebSocketSession(session);
        webSocketDO.setJSch(jSch);

        webSocketSessions.put(session.getId(), webSocketDO);

    }

    /**
     * 处理来自xterm发来的字符
     * @param payload
     * @param session
     */
    public void handleMsgFromXterm(String payload, WebSocketSession session) {
        try {
            WebSocketMessage<String> message = JSON.parseObject(payload, new TypeReference<WebSocketMessage<String>>(){});

            // xterm发起连接请求信息
            if (StringUtils.equals(message.getType(), WebSocketMessageType.XTERM_SSH_INIT.getValue())) {
                this.connectionInfo = JSON.parseObject(message.getData(), new TypeReference<SshConnectionInfo>(){});

                WebSocketDO webSocketDO = webSocketSessions.get(session.getId());

                if (Objects.nonNull(webSocketDO)) {
                    // 丢给线程进行处理
                    this.executorService.execute(() -> {
                        try {
                            connectToSsh(webSocketDO, this.connectionInfo, session);
                        } catch (Exception e) {
                            log.error("ssh连接出错", e);
                        }
                    });
                }

            }

            // 处理xterm输入的字符
            if (StringUtils.equals(message.getType(), WebSocketMessageType.XTERM_SSH_DATA.getValue())) {
                WebSocketDO webSocketDO = webSocketSessions.get(session.getId());
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
     * @param webSocketDO
     * @param connectionInfo
     * @param session
     */
    public void connectToSsh(WebSocketDO webSocketDO, SshConnectionInfo connectionInfo, WebSocketSession session) {

        Properties config = new Properties();
        // jsch的特性,如果没有这个配置,无法完成ssh连接
        config.put("StrictHostKeyChecking", "no");

        Session jSchSession = null;

        try {

            JSch jSch = webSocketDO.getJSch();
            jSchSession = jSch.getSession(connectionInfo.getUsername(), connectionInfo.getHost(), connectionInfo.getPort());

            jSchSession.setConfig(config);
            jSchSession.setPassword(connectionInfo.getPasswd());
            jSchSession.connect(30 * 1000);

            Channel channel = jSchSession.openChannel(JSchChannelType.SHELL.getValue());
            channel.connect(30 * 1000);

            webSocketDO.setChannel(channel);

            sendToSsh (channel, "\r\n");

            // 读取shell的数据
            InputStream input = channel.getInputStream();
            byte[] buf = new byte[32 * 1024];

            while (true) {
                int length = input.read(buf);
                if (length < 0) {
                    sendToWebSocket(session, "ssh链接已断开".getBytes(Charset.forName("UTF-8")));
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
     * @param channel
     * @param data
     * @throws IOException
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
     * @param session
     * @param buffer
     * @throws IOException
     */
    public void sendToWebSocket(WebSocketSession session, byte[] buffer) throws IOException {
        session.sendMessage(new TextMessage(buffer));
    }

    /**
     * 关闭WebSocket连接,并关闭ssh连接
     * @param session
     */
    public void close(WebSocketSession session) {
        WebSocketDO webSocketDO = webSocketSessions.get(session.getId());
        if (Objects.nonNull(webSocketDO)) {
            if (Objects.nonNull(webSocketDO.getChannel())) {
                webSocketDO.getChannel().disconnect();
            }
            webSocketSessions.remove(session.getId());
        }
    }

}
