package com.banzhe.hokage.biz.enums;

/**
 * @author linyimin
 * @date 2020/8/24 12:16 上午
 * @email linyimin520812@gmail.com
 * @description
 */
public enum UserErrorCodeEnum {

    USERNAME_NULL_ERROR("A-0001", "用户名不能为空"),

    USERNAME_DUPLICATE_ERROR("A-0002", "用户邮箱已存在"),

    USER_REGISTER_FAIL("A-0003", "用户注册失败");

    private String code;
    private String msg;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

    UserErrorCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
