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
                .excludePathPatterns("/app/login")
                .excludePathPatterns("/static/**")
                .excludePathPatterns("/index")
                .excludePathPatterns("/index.html")
                .excludePathPatterns("/**/*.png")
                .excludePathPatterns("/**/*.js")
                .excludePathPatterns("/**/*.css")
                .excludePathPatterns("/**/*.json")
                .excludePathPatterns("/**/*.ico")
                .excludePathPatterns("/**/*.txt");
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
