package com.hokage.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author yiminlin
 * @date 2021/06/29 8:54 pm
 * @description command aspect
 **/
@Aspect
@Component
public class CommandAspect {
    @Around("execution(* com.hokage.ssh.command.Command.*(..))")
    public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
        String export = "export PATH=/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin:/root/bin; ";
        Object returnValue = joinPoint.proceed(joinPoint.getArgs());
        if (returnValue instanceof String) {
            return export + returnValue;
        }
        return returnValue;
    }
}
