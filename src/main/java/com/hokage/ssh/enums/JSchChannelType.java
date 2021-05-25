package com.hokage.ssh.enums;

/**
 * channel type
 * @author yiminlin
 */
public enum  JSchChannelType {

    /**
     * shell
     */
    SHELL("shell"),
    EXEC("exec");

    private final String value;
    JSchChannelType(String value) {
        this.value = value;
    }

    public String getValue () {
        return this.value;
    }
}
