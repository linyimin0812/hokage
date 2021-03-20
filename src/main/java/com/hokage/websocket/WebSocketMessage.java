package com.hokage.websocket;

import lombok.Data;

/**
 * @author yiminlin
 */
@Data
public class WebSocketMessage<T> {
    /**
     * message content
     */
    private T data;
    /**
     * message type
     */
    private String type;

}
