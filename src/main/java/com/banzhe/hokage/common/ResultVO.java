package com.banzhe.hokage.common;

import lombok.Data;

/**
 * @author linyimin
 * @date 2020/8/23 1:12 上午
 * @email linyimin520812@gmail.com
 * @description 自定义统一响应体
 */
@Data
public class ResultVO<T> {

    // 表示响应成功
    private boolean success;
    /**
     * 状态码(A/B/C)-(0000-9999)
     * A类状态码：来源于用户
     * B类状态码：来源于当前系统
     * C类状态码：来源于第三方服务
     * 正常状态码： 00000
     */
    private String code;

    /**
     * 响应消息，用于说明响应情况
     */
    private String msg;

    /**
     * 响应具体数据
     */
    private T data;

    public ResultVO(boolean success, String code, String msg, T data) {
        this.success = success;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
