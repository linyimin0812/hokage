package com.hokage.ssh;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hokage.biz.service.HokageServerService;
import com.hokage.common.ServiceResponse;
import com.hokage.infra.worker.ThreadPoolWorker;
import com.hokage.persistence.dataobject.HokageServerDO;
import com.hokage.websocket.WebSocketSessionAndSshClient;
import com.hokage.websocket.WebSocketMessage;
import com.hokage.websocket.enums.WebSocketMessageType;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    /**
     * sessionId --> WebSocketAndSshSession
     */
    private static final Map<String, WebSocketSessionAndSshClient> WEB_SOCKET_SESSIONS = new ConcurrentHashMap<>();

    /**
     * ip_port_account --> List<WebSocketAndSshSession>
     */
    private static final Map<String, List<WebSocketSessionAndSshClient>> SSH_SOCKET_SESSIONS = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        receiveFromSsh();
    }


    /**
     * @param session ws session
     */
    public void add(WebSocketSession session) {
        WebSocketSessionAndSshClient client = new WebSocketSessionAndSshClient();
        client.setWebSocketSession(session);
        WEB_SOCKET_SESSIONS.put(session.getId(), client);
    }

    /**
     * 处理来自xterm发来的字符
     * @param message message content
     * @param session ws session
     */
    public void handleMsgFromXterm(WebSocketMessage<String> message, WebSocketSession session) throws JSchException {
        try {

            // xterm发起连接请求信息
            if (StringUtils.equals(message.getType(), WebSocketMessageType.XTERM_SSH_INIT.getValue())) {
                initSshClient(session, message);
            }

            // 处理xterm输入的字符
            if (StringUtils.equals(message.getType(), WebSocketMessageType.XTERM_SSH_DATA.getValue())) {
                sendToSsh(session, message);
            }
        } catch (Exception e) {
            log.error("handle message exception", e);
            this.close(session);
        }
    }

    private void initSshClient(WebSocketSession session, WebSocketMessage<String> message) {
        SshContext context = acquireSshContext(message.getData());
        WebSocketSessionAndSshClient websocketAndClient = WEB_SOCKET_SESSIONS.get(session.getId());

        if (Objects.nonNull(websocketAndClient)) {
            try {
                websocketAndClient = connectToSsh(websocketAndClient, context, session);
                WEB_SOCKET_SESSIONS.put(session.getId(), websocketAndClient);
            } catch (Exception e) {
                log.error("ssh连接出错", e);
                WEB_SOCKET_SESSIONS.remove(session.getId());
            }
        }
    }

    private SshContext acquireSshContext(String data) {
        SshContext context = JSON.parseObject(data, new TypeReference<SshContext>(){});
        // retrieve ssh password
        ServiceResponse<List<HokageServerDO>> response = serverService.selectByIds(Collections.singletonList(context.getId()));
        if (!response.getSucceeded() || CollectionUtils.isEmpty(response.getData())) {
            throw new RuntimeException("server is not exist. connection information: " + JSON.toJSONString(context));
        }
        String password = response.getData().get(0).getPasswd();
        context.setPasswd(password);
        return context;
    }

    /**
     * ssh连接,并启动一个线程,监听ssh返回的数据,并发送到WebSocket客户端,x-term进行展示
     * @param socketAndSshClient socket and ssh session
     * @param context ssh connection information
     * @param session websocket session
     */
    public WebSocketSessionAndSshClient connectToSsh(WebSocketSessionAndSshClient socketAndSshClient, SshContext context, WebSocketSession session) throws IOException {
        try {
            socketAndSshClient.setSshClient(new SshClient(context));
            return socketAndSshClient;
        } catch (Exception e) {
            log.error("connect to ssh error", e);
            sendToWebSocket(session, e.getMessage().getBytes(StandardCharsets.UTF_8));
            session.close();
            return null;
        } finally {
            WEB_SOCKET_SESSIONS.remove(session.getId());
        }
    }

    /**
     * 将前端通过websocket传过来的数据通过ssh转给服务器
     * @param session web socket session
     * @param message message from xterm
     * @throws IOException exception
     */
    public void sendToSsh(WebSocketSession session, WebSocketMessage<String> message) throws IOException, JSchException {
        WebSocketSessionAndSshClient websocketAndClient = WEB_SOCKET_SESSIONS.get(session.getId());
        SshClient client = websocketAndClient.getSshClient();
        ChannelShell shell = client.getShell();

        OutputStream output = shell.getOutputStream();
        output.write(message.getData().getBytes());
        output.flush();
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
    public void close(WebSocketSession session) throws JSchException {
        WebSocketSessionAndSshClient socketAndClient = WEB_SOCKET_SESSIONS.get(session.getId());
        if (Objects.nonNull(socketAndClient)) {
            SshClient client = socketAndClient.getSshClient();
            if (Objects.nonNull(client) && Objects.nonNull(client.getShell())) {
                client.getShell().disconnect();
            }
            WEB_SOCKET_SESSIONS.remove(session.getId());
        }
    }

    private void receiveFromSsh() {
        int coreNum = Runtime.getRuntime().availableProcessors();
        IntStream.range(0, coreNum).forEach(index -> {
            worker.getExecutorPool().execute(() -> {
                while (true) {
                    List<WebSocketSessionAndSshClient> clients = acquireActiveSshClient();
                    int pageSize = clients.size() / coreNum + 1;
                    int fromIndex = pageSize * index;
                    if (fromIndex >= clients.size()) {
                        continue;
                    }
                    int toIndex = pageSize * (index + 1);
                    if (toIndex > clients.size()) {
                        toIndex = clients.size();
                    }
                    clients = clients.subList(fromIndex, toIndex);
                    for (WebSocketSessionAndSshClient socketAndClient : clients) {
                        processReceiveMessageFromSsh(socketAndClient);
                    }
                }
            });
        });
    }

    private List<WebSocketSessionAndSshClient> acquireActiveSshClient() {
        synchronized (this) {
            return WEB_SOCKET_SESSIONS.values().stream()
                    .filter(socketAndClient -> {
                        SshClient sshClient = socketAndClient.getSshClient();
                        try {
                            return Objects.nonNull(sshClient) && sshClient.getShell().isConnected();
                        } catch (JSchException e) {
                            SshContext context = sshClient.getContext();
                            log.warn(String.format("account: %s, ip: %s, port: %s is not connected.", context.getAccount(), context.getIp(), context.getSshPort() ), e);
                            return false;
                        }
                    }).collect(Collectors.toList());
        }
    }

    private void processReceiveMessageFromSsh(WebSocketSessionAndSshClient socketAndClient) {
        SshClient client = socketAndClient.getSshClient();
        WebSocketSession session = socketAndClient.getWebSocketSession();
        try {
            ChannelShell shell = client.getShell();
            // 读取shell的数据
            InputStream input = shell.getInputStream();
            byte[] buf = new byte[32 * 1024];
            do {
                while (input.available() > 0) {
                    int length = input.read(buf);
                    if (length < 0) {
                        sendToWebSocket(session, "\r\nssh链接已断开".getBytes(StandardCharsets.UTF_8));
                        break;
                    }
                    sendToWebSocket(session, Arrays.copyOfRange(buf, 0, length));
                }
            } while (!shell.isClosed());
        } catch (Exception e) {
            SshContext context = client.getContext();
            log.error(String.format("retrieve shell error. account: %s, ip: %s, port: %s.", context.getAccount(), context.getIp(), context.getSshPort() ), e);
        }
    }

}
