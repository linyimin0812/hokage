package com.hokage.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hokage.ssh.SshComponent;
import com.hokage.websocket.enums.WebSocketMessageType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yiminlin
 */
@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private SshComponent sshService;

    private static final char DEL_ENCODE = '\u007F';

    private Map<WebSocketSession, StringBuilder> sessionCommand = new HashMap<>();
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
        WebSocketMessage<String> textMessage = JSON.parseObject(message.getPayload(), new TypeReference<WebSocketMessage<String>>(){});
        sshService.handleMsgFromXterm(textMessage, session);
        logCommand(session, textMessage);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessionCommand.remove(session);
    }

    /**
     * log command
     * @param session websocket connection session
     * @param textMessage message from web terminal
     */
    private void logCommand(WebSocketSession session, WebSocketMessage<String> textMessage) {

        if (StringUtils.equals(WebSocketMessageType.XTERM_SSH_INIT.getValue(), textMessage.getType())) {
            return;
        }

        if (!sessionCommand.containsKey(session)) {
            sessionCommand.put(session, new StringBuilder());
        }
        StringBuilder sb = sessionCommand.get(session);
        // remove character
        if (textMessage.getData().charAt(0) == DEL_ENCODE && sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        } else if (StringUtils.equals(textMessage.getData(), StringUtils.CR)) {
            log.info("recv command: {}", sessionCommand.get(session).toString());
            sessionCommand.put(session, new StringBuilder());
        } else {
            sb.append(textMessage.getData());
        }
    }

}
