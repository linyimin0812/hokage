package com.hokage.common;

/**
 * @author linyimin
 * @date 2020/8/23 1:11 am
 * @email linyimin520812@gmail.com
 * @description
 */
public class BaseController {
    /**
     * normal response
     * @param data response content
     * @param <T> response content type
     * @return this
     */
    protected  <T> ResultVO<T> success(T data) {
        return new ResultVO<>(true, "00000", null, data);
    }

    /**
     * fail response
     * @param code result code
     * @param msg fail message
     * @param <T> response content type
     * @return this
     */
    protected <T> ResultVO<T> fail(String code, String msg) {
        return new ResultVO<>(false, code, msg, null);
    }

    protected <T> ResultVO<T> response(ServiceResponse<T> response) {
        if (response.getSucceeded()) {
            return success(response.getData());
        }
        return fail(response.getCode(), response.getMsg());
    }

}
