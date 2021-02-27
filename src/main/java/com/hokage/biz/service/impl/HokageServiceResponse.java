package com.hokage.biz.service.impl;


import com.hokage.biz.enums.ResultCodeEnum;
import com.hokage.common.ServiceResponse;

/**
 * @author linyimin
 * @date 2021/2/27 20:10
 * @email linyimin520812@gmail.com
 * @description
 */
public class HokageServiceResponse {
    /**
     * normal response
     * @param data
     * @param <T>
     * @return
     */
    protected  <T> ServiceResponse<T> success(T data) {
        ServiceResponse<T> serviceResponse = new ServiceResponse<>();
        serviceResponse.setSucceeded(true)
                .setCode(ResultCodeEnum.SUCCESS.getCode())
                .setMsg(ResultCodeEnum.SUCCESS.getMsg())
                .setData(data);
        return serviceResponse;
    }

    /**
     * fail response
     * @param code
     * @param msg
     * @param <T>
     * @return
     */
    protected <T> ServiceResponse<T> fail(String code, String msg) {
        ServiceResponse<T> serviceResponse = new ServiceResponse<>();
        serviceResponse.setSucceeded(false)
                .setCode(code)
                .setMsg(msg)
                .setData(null);
        return serviceResponse;
    }
}
