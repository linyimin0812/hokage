package com.hokage.biz.enums;

/**
 * @author yiminlin
 */

public enum LoginTypeEnum {
    /**
     * password
     */
    password(0),
    /**
     * private key file
     */
    key(1);

    private final Integer value;

    public Integer getValue() {
        return this.value;
    }

    LoginTypeEnum(Integer value) {
        this.value = value;
    }
}
