package com.banzhe.hokage.biz.enums;

/**
 * @author linyimin
 * @date 2020/8/30 3:44 下午
 * @email linyimin520812@gmail.com
 * @description 用户角色
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

    private Integer value;

    public Integer getValue() {
        return this.value;
    }

    UserRoleEnum(Integer value) {
        this.value = value;
    }
}
