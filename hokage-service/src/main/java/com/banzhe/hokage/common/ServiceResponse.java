package com.banzhe.hokage.common;

import com.banzhe.hokage.biz.enums.SuccessCodeEnum;
import lombok.Data;

/**
 * @author linyimin
 * @date 2020/8/30 3:49 下午
 * @email linyimin520812@gmail.com
 * @description 服务层返回
 */
@Data
public class ServiceResponse<T> {
    private T data; // 服务返回数据
    private Boolean succeeded;  // 服务调用是否成功
    private String msg; // 服务调用失败时返回信息
    private String code; // 错误码

    public ServiceResponse<T> success(T data) {
        this.data = data;
        this.succeeded = Boolean.TRUE;
        this.msg = null;
        this.code = SuccessCodeEnum.success.getCode();
        return this;
    }

    public ServiceResponse success() {
        this.succeeded = Boolean.TRUE;
        this.msg = null;
        this.code = SuccessCodeEnum.success.getCode();
        return this;
    }

    public ServiceResponse<T> fail(String code, String msg) {
        this.code = code;
        this.msg = msg;
        this.succeeded = Boolean.FALSE;
        return this;
    }
}
