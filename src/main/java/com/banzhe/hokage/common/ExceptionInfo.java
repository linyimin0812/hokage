package com.banzhe.hokage.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author linyimin
 * @date 2020/8/23 1:59 am
 * @email linyimin520812@gmail.com
 * @description custom parameter verification error code and error message annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ExceptionInfo {
    String code();
    String msg() default "";
}
