package com.hokage.websocket;

import com.hokage.ssh.SshComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * @author yiminlin
 */
@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private SshComponent sshService;

    @Autowired
    public void setSshService(SshComponent service) {
        this.sshService = service;
    }

    @Override
    public void afterConnectionEstablished(@NonNull  WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sshService.add(session);
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws Exception {
        log.info("recv msg: {}", message.getPayload());
        sshService.handleMsgFromXterm(message.getPayload(), session);
        super.handleTextMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }
}
