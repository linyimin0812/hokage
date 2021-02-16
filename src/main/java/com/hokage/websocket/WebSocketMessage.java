package com.hokage.websocket;

import lombok.Data;

@Data
public class WebSocketMessage<T> {
    /**
     * 发送的信息内容
     */
    private T data;
    /**
     * 消息类型
     */
    private String type;

}
