package com.banzhe.hokage.biz.enums;

/**
 * @author linyimin
 * @date 2020/8/30 5:34 pm
 * @email linyimin520812@gmail.com
 * @description
 */
public enum SequenceErrorCodeEnum {

    /**
     * sequence value insert error
     */
    SEQUENCE_INSERT_ERROR("A-0101", "sequence insert error");

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

    SequenceErrorCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
