package com.hokage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author linyimin
 * @date 2020/8/30 3:27 pm
 * @email linyimin520812@gmail.com
 * @description password encode
 */
@Configuration
public class PasswordConfig {
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
