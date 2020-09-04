package com.banzhe.hokage.config;

import com.banzhe.hokage.biz.interceptor.AuthCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author linyimin
 * @date 2020/9/3 9:05 下午
 * @email linyimin520812@gmail.com
 * @description
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthCheckInterceptor()).excludePathPatterns(Arrays.asList(
                "/user/login",
                "/#/app/index",
                "/"
        ));
    }
}
