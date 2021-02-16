package com.hokage.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @author linyimin
 * @date 2020/8/23 2:04 am
 * @email linyimin520812@gmail.com
 * @description global exception handler
 */
@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultVO<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) throws NoSuchFieldException {
        // retrieve error message from the first parameter
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        // class object of the first exception parameter
        Class<?> parameterType = e.getParameter().getParameterType();
        // the first exception parameter name
        String parameterName = e.getBindingResult().getFieldError().getField();

        // retrieve field property by name
        Field field = parameterType.getDeclaredField(parameterName);

        // retrieve annotation message from field
        ExceptionInfo exceptionInfo = field.getAnnotation(ExceptionInfo.class);

        message = StringUtils.isEmpty(exceptionInfo.msg()) ? message : exceptionInfo.msg();

        if (Objects.nonNull(exceptionInfo)) {
            return new ResultVO<>(false, exceptionInfo.code(), message, null);
        }
        // retrieve first error(A-XXXX: means that there are no define error code)
        return new ResultVO<>(false, "A-XXXX", message, null);
    }
}
