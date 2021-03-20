package com.hokage.websocket;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

/**
 * one socket connection corresponds to one ssh connection
 * @author yiminlin
 */
@Data
public class WebSocketAndSshSession {
    private WebSocketSession webSocketSession;
    private JSch jSch;
    /**
     * jsch channel
     */
    private Channel channel;
}
