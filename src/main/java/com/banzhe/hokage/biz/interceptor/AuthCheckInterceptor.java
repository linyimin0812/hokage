package com.banzhe.hokage.biz.interceptor;

import com.banzhe.hokage.biz.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * @author linyimin
 * @date 2020/9/3 8:48 下午
 * @email linyimin520812@gmail.com
 * @description 登录校验拦截器
 */
public class AuthCheckInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        String path = request.getRequestURI();

        /**
         * 用户已经登录
         */
        if (Objects.nonNull(session)) {
            Object user = session.getAttribute(Constant.USER_SESSION_KEY);
            if (Objects.nonNull(user)) {
                if (StringUtils.equals(path, Constant.LOGIN_PAGE_URL)) {
                    response.sendRedirect(Constant.INDEX_URL);
                }
                return true;
            }
        }

        if (StringUtils.equals(path, Constant.LOGIN_PAGE_URL)) {
            return true;
        }

        /**
         * 用户未登录
         */
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.sendRedirect(Constant.LOGIN_PAGE_URL);
        return false;
    }
}
