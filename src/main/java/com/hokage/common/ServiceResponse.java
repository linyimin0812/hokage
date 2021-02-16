package com.hokage.common;

import com.hokage.biz.enums.SuccessCodeEnum;
import lombok.Data;

/**
 * @author linyimin
 * @date 2020/8/30 3:49 pm
 * @email linyimin520812@gmail.com
 * @description service layer return
 */
@Data
public class ServiceResponse<T> {
    /**
     * data of service return
     */
    private T data;
    /**
     * is service success
     */
    private Boolean succeeded;
    /**
     * error messsage
     */
    private String msg;
    /**
     * service return code
     */
    private String code;

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
