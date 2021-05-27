package com.hokage.websocket.enums;

/**
 * 所有WebSocket消息类型
 * @author yiminlin
 */
public enum WebSocketMessageType {

    /**
     * x-term request connection info
     */
    XTERM_SSH_INIT("xtermSshInit"),
    /**
     * command from
     */
    XTERM_SSH_DATA("xtermSshData"),

    /**
     * x-term resize
     */
    XTERM_SSH_RESIZE("xtermSshResize");

    private final String value;

    WebSocketMessageType (String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
