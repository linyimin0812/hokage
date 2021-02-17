package com.hokage.common;

import lombok.Data;

/**
 * @author linyimin
 * @date 2020/8/23 1:12 am
 * @email linyimin520812@gmail.com
 * @description custom unified response body
 */
@Data
public class ResultVO<T> {

    /**
     * controller run success
     */
    private Boolean success;
    /**
     * status(A/B/C)-(0000-9999)
     * A class status：from user
     * B class status：from current system
     * C class status：from third party service
     * formal status： 00000
     */
    private String code;

    /**
     * response message，for describing the error
     */
    private String msg;

    /**
     * response data
     */
    private T data;

    public ResultVO(boolean success, String code, String msg, T data) {
        this.success = success;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
