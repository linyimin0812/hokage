package com.banzhe.hokage.biz.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * @author linyimin
 * @date 2020/9/3 8:48 下午
 * @email linyimin520812@gmail.com
 * @description
 */
public class AuthCheckInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        /**
         * 用户已经登录
         */
        if (Objects.nonNull(session)) {
            Object user = session.getAttribute("userInfo");
            if (Objects.nonNull(user)) {
                return true;
            }
        }

        /**
         * 用户未登录
         */
        response.setStatus(response.SC_UNAUTHORIZED);
        response.sendRedirect("/#/app/index");
        return false;
    }
}
