package com.banzhe.hokage.config;

import com.banzhe.hokage.biz.interceptor.AuthCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.Arrays;

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
        registry.addInterceptor(new AuthCheckInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(Arrays.asList( // 静态资源不需要拦截
                        "/static/**",
                        "/index",
                        "/index.html",
                        "/**/*.png",
                        "/**/*.js",
                        "/**/*.css",
                        "/**/*.json",
                        "/**/*.ico",
                        "/**/*.txt",
                        "/error"                    // springboot默认的异常处理机制，发送/error请求，所以不能拦截
                ))
                .excludePathPatterns(Arrays.asList( // 用户的登录注册不需要拦截
                        "/user/register",
                        "/user/login",
                        "/user/status"
                ));
    }
    // TODO: 为什么配了ResourceHandler，就不能访问了
    /**
     * 配置静态访问资源
     * @param registry
     */
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/templates/");
//    }

}
