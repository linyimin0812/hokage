package com.hokage.websocket;

import com.hokage.ssh.SshClient;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

/**
 * one socket connection corresponds to one ssh connection
 * @author yiminlin
 */
@Data
public class WebSocketSessionAndSshClient {
    private WebSocketSession webSocketSession;
    private SshClient sshClient;
    private boolean processing;
}
