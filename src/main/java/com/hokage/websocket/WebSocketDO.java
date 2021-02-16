package com.hokage.websocket;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

/**
 * 一个socket连接对应一个ssh连接
 */
@Data
public class WebSocketDO {
    private WebSocketSession webSocketSession;
    private JSch jSch;
    private Channel channel;    // shell channel
}
