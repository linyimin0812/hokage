package com.banzhe.hokage.common;

/**
 * @author linyimin
 * @date 2020/8/23 1:11 am
 * @email linyimin520812@gmail.com
 * @description
 */
public class BaseController {
    /**
     * normal response
     * @param data
     * @param <T>
     * @return
     */
    protected  <T> ResultVO<T> success(T data) {
        ResultVO<T> resultVO = new ResultVO<>(true, "00000", null, data);
        return resultVO;
    }

    /**
     * fail response
     * @param code
     * @param msg
     * @param <T>
     * @return
     */
    protected <T> ResultVO<T> fail(String code, String msg) {
        ResultVO<T> resultVO = new ResultVO<>(false, code, msg, null);
        return resultVO;
    }

}
