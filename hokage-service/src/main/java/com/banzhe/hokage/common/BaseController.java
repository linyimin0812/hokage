package com.banzhe.hokage.common;

/**
 * @author linyimin
 * @date 2020/8/23 1:11 上午
 * @email linyimin520812@gmail.com
 * @description
 */
public class BaseController {
    /**
     * 正常响应
     * @param data
     * @param <T>
     * @return
     */
    protected  <T> ResultVO<T> success(T data) {
        ResultVO<T> resultVO = new ResultVO<>("00000", null, data);
        return resultVO;
    }

    /**
     * 失败响应
     * @param code
     * @param msg
     * @param <T>
     * @return
     */
    protected <T> ResultVO<T> fail(String code, String msg) {
        ResultVO<T> resultVO = new ResultVO<>(code, msg, null);
        return resultVO;
    }

}
