package com.hokage.ssh.enums;

public enum  JSchChannelType {

    SHELL("shell");

    private String value;
    JSchChannelType(String value) {
        this.value = value;
    }

    public String getValue () {
        return this.value;
    }
}
