package com.hokage.ssh.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Stopwatch;
import com.hokage.biz.service.HokageServerService;
import com.hokage.common.ServiceResponse;
import com.hokage.infra.worker.ScheduledThreadPoolWorker;
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
public class SshShellComponent {

    private HokageServerService serverService;
    private SshShellThreadPoolWorker worker;
    private ScheduledThreadPoolWorker scheduledServiceWorker;

    @Value("${ssh.shell.result.reading.timeout.millis}")
    private int shellResultReadingTimeoutMillis;
    @Value("${ssh.shell.process.interval.millis}")
    private int shellProcessIntervalMillis;

    @Autowired
    public void setServerService(HokageServerService serverService) {
        this.serverService = serverService;
    }

    @Autowired
    public void setPool(SshShellThreadPoolWorker worker) {
        this.worker = worker;
    }

    @Autowired
    public void setScheduledServiceWorker(ScheduledThreadPoolWorker worker) {
        this.scheduledServiceWorker = worker;
    }

    /**
     * sessionId --> WebSocketAndSshSession
     */
    private static final Map<String, WebSocketSessionAndSshClient> WEB_SOCKET_SESSIONS = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        int coreNum = Runtime.getRuntime().availableProcessors();
        scheduledServiceWorker.getScheduledService().scheduleAtFixedRate(() -> {
            if (WEB_SOCKET_SESSIONS.isEmpty()) {
                return;
            }
            int channelShellNum = acquireActiveSshClient().size();
            int pageSize = channelShellNum / coreNum + 1;
            try {
                receiveFromSsh(pageSize);
            } catch (Exception e) {
                log.error("scheduledServiceWorker.getScheduledService().scheduleAtFixedRate error. err: {}", e.getMessage());
            }
        }, 0, shellProcessIntervalMillis, TimeUnit.MILLISECONDS);
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
        }
    }

    /**
     * resize ssh terminal
     * @param session websocket session
     * @param message window size from web ssh terminal
     * @throws Exception ssh operate exception
     */
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

    /**
     * init a ssh client
     * @param session websocket session
     * @param message ssh connection information from web ssh terminal
     */
    private void initSshClient(WebSocketSession session, WebSocketMessage<String> message) {
        SshContext context = acquireSshContext(message.getData());
        WebSocketSessionAndSshClient websocketAndClient = WEB_SOCKET_SESSIONS.get(session.getId());

        if (Objects.nonNull(websocketAndClient)) {
            try {
                websocketAndClient = connectToSsh(websocketAndClient, context, session);
                if (Objects.isNull(websocketAndClient)) {
                    return;
                }
                WEB_SOCKET_SESSIONS.put(session.getId(), websocketAndClient);
            } catch (Exception e) {
                log.error("SshShellComponent.initSshClient error. err: {}", e.getMessage());
                WEB_SOCKET_SESSIONS.remove(session.getId());
            }
        }
    }

    /**
     * acquire ssh connection password and assemble as ssh context
     * @param data ssh connection from web ssh terminal
     * @return ssh context
     */
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
        WEB_SOCKET_SESSIONS.remove(session.getId());
        if (Objects.isNull(socketAndClient)) {
            return;
        }
        SshClient client = socketAndClient.getSshClient();
        if (Objects.isNull(client)) {
            return;
        }
        Session sshSession = client.getSession();
        if (Objects.isNull(sshSession)) {
            return;
        }
        ChannelShell shell = client.getShell();
        if (Objects.nonNull(shell)) {
            shell.disconnect();
        }
        sshSession.disconnect();
    }

    /**
     * handle message from ssh
     * @param pageSize page size, how many client can process by a thread
     */
    private void receiveFromSsh(int pageSize) {
        int coreNum = Runtime.getRuntime().availableProcessors();
        final List<WebSocketSessionAndSshClient> clients = acquireActiveSshClient();
        IntStream.range(0, coreNum).forEach(index -> {
            List<WebSocketSessionAndSshClient> subClients = pageClients(clients, pageSize, index);
            if (CollectionUtils.isEmpty(subClients)) {
                return;
            }
            dispatchClient(subClients);
        });
    }

    /**
     * acquire active ssh client
     * @return active ssh client list
     */
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

    /**
     * handle message from ssh input stream
     * use timeout to avoid blocking
     * @param socketAndClient websocket session and ssh client
     */
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

    /**
     * page client
     * @param clients all activate ssh client
     * @param pageNum from 0
     * @param pageSize page size, how many client can process by a thread
     * @return client sublist
     */
    private List<WebSocketSessionAndSshClient> pageClients(List<WebSocketSessionAndSshClient> clients, int pageSize,  int pageNum) {
        int fromIndex = pageSize * pageNum;
        if (fromIndex >= clients.size()) {
            return Collections.emptyList();
        }
        int toIndex = pageSize * (pageNum + 1);
        if (toIndex > clients.size()) {
            toIndex = clients.size();
        }
        return clients.subList(fromIndex, toIndex);
    }

    /**
     * dispatch ssh input stream to thread
     * @param clients sublist client
     */
    private void dispatchClient(List<WebSocketSessionAndSshClient> clients) {
        try {
            worker.getExecutorPool().execute(() -> {
                for (WebSocketSessionAndSshClient socketAndClient : clients) {
                    processReceiveMessageFromSsh(socketAndClient);
                }
            });
        } catch (Exception e) {
            if (e instanceof RejectedExecutionException ) {
                for (WebSocketSessionAndSshClient client : clients) {
                    WebSocketSession webSocketSession = client.getWebSocketSession();
                    Session session = client.getSshClient().getSession();
                    try {
                        session.disconnect();
                        if (webSocketSession.isOpen()) {
                            webSocketSession.close();
                        }
                    } catch (Exception ignored) {

                    } finally {
                        WEB_SOCKET_SESSIONS.remove(webSocketSession.getId());
                    }
                }
            } else {
                log.error("worker.getExecutorPool().execute error. err: {}", e.getMessage());
            }
        }
    }
}
