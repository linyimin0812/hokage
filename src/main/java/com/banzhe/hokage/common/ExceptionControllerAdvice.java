package com.banzhe.hokage.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @author linyimin
 * @date 2020/8/23 2:04 上午
 * @email linyimin520812@gmail.com
 * @description 全局异常处理
 */
@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultVO<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) throws NoSuchFieldException {
        // 从第一个参数异常中取出错误信息
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        // 第一个异常参数的Class对象
        Class<?> parameterType = e.getParameter().getParameterType();
        // 第一个异常参数的名称
        String parameterName = e.getBindingResult().getFieldError().getField();

        // 根据名称获取字段属性
        Field field = parameterType.getDeclaredField(parameterName);

        // 获取字段上的注解信息
        ExceptionInfo exceptionInfo = field.getAnnotation(ExceptionInfo.class);

        message = StringUtils.isEmpty(exceptionInfo.msg()) ? message : exceptionInfo.msg();

        if (Objects.nonNull(exceptionInfo)) {
            return new ResultVO<>(false, exceptionInfo.code(), message, null);
        }
        // 获取第一个错误(A-XXXX表示没有定义错误码)
        return new ResultVO<>(false, "A-XXXX", message, null);
    }
}
