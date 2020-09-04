package com.banzhe.hokage.biz.enums;

/**
 * @author linyimin
 * @date 2020/8/30 3:44 下午
 * @email linyimin520812@gmail.com
 * @description 用户角色
 */
public enum UserRoleEnum {

    super_operator(0),  // 超级管理员
    supervisor(1),      // 管理员
    subordinate(2);     // 普通用户

    private Integer value;

    public Integer getValue() {
        return this.value;
    }

    UserRoleEnum(Integer value) {
        this.value = value;
    }
}
