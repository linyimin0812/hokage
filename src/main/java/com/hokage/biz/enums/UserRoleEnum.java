package com.hokage.biz.enums;

/**
 * @author linyimin
 * @date 2020/8/30 3:44 pm
 * @email linyimin520812@gmail.com
 * @description user role
 */
public enum UserRoleEnum {

    /**
     * super
     */
    super_operator(100),
    /**
     * supervisor
     */
    supervisor(1),
    /**
     * ordinary
     */
    subordinate(2);

    private final Integer value;

    public Integer getValue() {
        return this.value;
    }

    UserRoleEnum(Integer value) {
        this.value = value;
    }
}
