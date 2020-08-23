package com.banzhe.hokage.common;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author linyimin
 * @date 2020/8/23 2:04 上午
 * @email linyimin520812@gmail.com
 * @description 全局异常处理
 */
@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultVO<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        // 从异常中取出错误信息
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        return new ResultVO<>("0", null, null);

    }
}
