package com.banzhe.hokage.websocket.enums;

/**
 * 所有WebSocket消息类型
 */
public enum WebSocketMessageType {

    /**
     * x-term请求连接信息
     */
    XTERM_SSH_INIT("xtermSshInit"),
    /**
     * xterm发送的业务信息
     */
    XTERM_SSH_DATA("xtermSshData");

    private String value;

    WebSocketMessageType (String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
