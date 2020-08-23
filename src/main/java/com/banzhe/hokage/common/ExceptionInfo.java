package com.banzhe.hokage.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author linyimin
 * @date 2020/8/23 1:59 上午
 * @email linyimin520812@gmail.com
 * @description 自定义参数校验错误码和错误信息注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ExceptionInfo {
    String code();
    String msg();
}
