package com.hokage.ssh.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Stopwatch;
import com.hokage.biz.service.HokageServerService;
import com.hokage.common.ServiceResponse;
import com.hokage.infra.worker.SshShellThreadPoolWorker;
import com.hokage.persistence.dataobject.HokageServerDO;
import com.hokage.ssh.SshClient;
import com.hokage.ssh.context.SshContext;
import com.hokage.websocket.WebSocketSessionAndSshClient;
import com.hokage.websocket.WebSocketMessage;
import com.hokage.websocket.domain.TerminalSize;
import com.hokage.websocket.enums.WebSocketMessageType;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author yiminlin
 */

@Slf4j
@Component
public class SshShellComponent {

    private HokageServerService serverService;

    @Autowired
    public void setServerService(HokageServerService serverService) {
        this.serverService = serverService;
    }

    private SshShellThreadPoolWorker worker;

    @Value("${ssh.shell.channel.input.stream.per.thread}")
    private int channelShellNumPerThread;
    @Value("${ssh.shell.result.reading.timeout.millis}")
    private int shellResultReadingTimeoutMillis;

    @Autowired
    public void setPool(SshShellThreadPoolWorker worker) {
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

    @Scheduled(fixedDelay = 50)
    public void scheduleSshMessageProcessThread() {
        if (WEB_SOCKET_SESSIONS.isEmpty()) {
            return;
        }
        int channelShellNum = WEB_SOCKET_SESSIONS.size();
        int threadNum = channelShellNum  / channelShellNumPerThread <= 0 ? 1 : channelShellNum  / channelShellNumPerThread;
        receiveFromSsh(threadNum);
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
    public void handleMsgFromXterm(WebSocketMessage<String> message, WebSocketSession session) throws Exception {
        try {

            // xterm发起连接请求信息
            if (StringUtils.equals(message.getType(), WebSocketMessageType.XTERM_SSH_INIT.getValue())) {
                initSshClient(session, message);
                return;
            }

            // 处理xterm输入的字符
            if (StringUtils.equals(message.getType(), WebSocketMessageType.XTERM_SSH_DATA.getValue())) {
               sendToSsh(session, message);
                return;
            }

            // resize terminal
            if (StringUtils.equals(message.getType(), WebSocketMessageType.XTERM_SSH_RESIZE.getValue())) {
                resizeTerminal(session, message);
                return;
            }

            log.error("unsupported data: {}", message);

        } catch (Exception e) {
            log.error("handle message exception", e);
            this.close(session);
        }
    }

    private void resizeTerminal(WebSocketSession session, WebSocketMessage<String> message) throws Exception {
        if (!WEB_SOCKET_SESSIONS.containsKey(session.getId())) {
            return;
        }
        WebSocketSessionAndSshClient socketAndSshClient = WEB_SOCKET_SESSIONS.get(session.getId());
        if (Objects.isNull(socketAndSshClient.getSshClient()) || Objects.isNull(socketAndSshClient.getSshClient().getShellOrCreate())) {
            return;
        }
        ChannelShell shell = socketAndSshClient.getSshClient().getShellOrCreate();
        TerminalSize size = JSONObject.parseObject(message.getData(), TerminalSize.class);
        if (Objects.isNull(size.getCols()) || Objects.isNull(size.getRows())) {
            return;
        }
        shell.setPtySize(size.getCols(), size.getRows(), 640, 480);
    }

    private void initSshClient(WebSocketSession session, WebSocketMessage<String> message) {
        SshContext context = acquireSshContext(message.getData());
        WebSocketSessionAndSshClient websocketAndClient = WEB_SOCKET_SESSIONS.get(session.getId());

        if (Objects.nonNull(websocketAndClient)) {
            try {
                websocketAndClient = connectToSsh(websocketAndClient, context, session);
                WEB_SOCKET_SESSIONS.put(session.getId(), websocketAndClient);
            } catch (Exception e) {
                log.error("SshShellComponent.initSshClient error. err: {}", e.getMessage());
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
            SshClient client = new SshClient(context);
            client.getShellContextOrCreate();
            socketAndSshClient.setSshClient(client);
            return socketAndSshClient;
        } catch (Exception e) {
            log.error("connect to ssh error. err: {}", e.getMessage());
            sendToWebSocket(session, (e.getMessage() + "\r\n").getBytes(StandardCharsets.UTF_8));
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
    public void sendToSsh(WebSocketSession session, WebSocketMessage<String> message) throws Exception {
        WebSocketSessionAndSshClient websocketAndClient = WEB_SOCKET_SESSIONS.get(session.getId());
        SshClient client = websocketAndClient.getSshClient();
        ChannelShell shell = client.getShellOrCreate();

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
    public void close(WebSocketSession session) throws Exception {
        WebSocketSessionAndSshClient socketAndClient = WEB_SOCKET_SESSIONS.get(session.getId());
        if (Objects.nonNull(socketAndClient)) {
            SshClient client = socketAndClient.getSshClient();
            if (Objects.nonNull(client) && Objects.nonNull(client.getShell())) {
                client.getShell().disconnect();
            }
            WEB_SOCKET_SESSIONS.remove(session.getId());
        }
    }

    private void receiveFromSsh(int threadNum) {
        IntStream.range(0, threadNum).forEach(index -> {
            List<WebSocketSessionAndSshClient> clients = acquireActiveSshClient();
            int fromIndex = channelShellNumPerThread * index;
            if (fromIndex >= clients.size()) {
                return;
            }
            int toIndex = channelShellNumPerThread * (index + 1);
            if (toIndex > clients.size()) {
                toIndex = clients.size();
            }
            clients = clients.subList(fromIndex, toIndex);

            if (CollectionUtils.isEmpty(clients)) {
                return;
            }

            List<WebSocketSessionAndSshClient> finalClients = clients;
            worker.getExecutorPool().execute(() -> {
                for (WebSocketSessionAndSshClient socketAndClient : finalClients) {
                    processReceiveMessageFromSsh(socketAndClient);
                }
            });
        });
    }

    private List<WebSocketSessionAndSshClient> acquireActiveSshClient() {
        synchronized (this) {
            return WEB_SOCKET_SESSIONS.values().stream()
                    .filter(socketAndClient -> {
                        if (socketAndClient.isProcessing()) {
                            return false;
                        }
                        SshClient sshClient = socketAndClient.getSshClient();
                        try {
                            return Objects.nonNull(sshClient) && Objects.nonNull(sshClient.getShell()) && sshClient.getShell().isConnected();
                        } catch (Exception e) {
                            SshContext context = sshClient.getSshContext();
                            log.warn(String.format("account: %s, ip: %s, port: %s is not connected.", context.getAccount(), context.getIp(), context.getSshPort() ), e);
                            return false;
                        }
                    }).collect(Collectors.toList());
        }
    }

    private void processReceiveMessageFromSsh(WebSocketSessionAndSshClient socketAndClient) {
        SshClient client = socketAndClient.getSshClient();
        WebSocketSession session = socketAndClient.getWebSocketSession();
        socketAndClient.setProcessing(true);
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            // 读取shell的数据
            InputStream input = client.getShellContextOrCreate().getInputStream();
            byte[] buf = new byte[32 * 1024];
            boolean timeout = false;
            while (input.available() > 0 && !timeout) {
                int length = input.read(buf);
                if (length < 0) {
                    sendToWebSocket(session, "\r\nssh链接已断开".getBytes(StandardCharsets.UTF_8));
                    break;
                }
                sendToWebSocket(session, Arrays.copyOfRange(buf, 0, length));
                timeout = stopwatch.elapsed(TimeUnit.MILLISECONDS) > shellResultReadingTimeoutMillis;
            }
        } catch (Exception e) {
            SshContext context = client.getSshContext();
            log.error(String.format("retrieve shell error. account: %s, ip: %s, port: %s.", context.getAccount(), context.getIp(), context.getSshPort() ), e);
        } finally {
            socketAndClient.setProcessing(false);
            stopwatch.stop();
        }
    }

}
