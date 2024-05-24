package com.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Component
public class EmpFilter implements HandlerInterceptor {



    String[] ignoreUrls = {"/emp/toLogin","/emp/doLogin","/emp/resetPasswordLink","/front_end/**","/websocket/**","/client_chat.html", "/mem/**","mem/memLogin", "/rental/**",

            "/**/*.js", "/**/*.css", "/**/*.jpg", "/**/*.jpeg", "/**/*.png", "/**/*.gif", "/**/*.svg","/**/*.ico"};
    private AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.getSession();//目的將JSESSIONID透過cookie傳回前端，方便後續的session追蹤
        Cookie[] cookies = request.getCookies();
        String contextPath = request.getContextPath();
        Optional<Cookie> loginAlready = null;
        if (!ObjectUtils.isEmpty(cookies)) {
            loginAlready = Arrays.stream(cookies).filter(cookie -> "loginAlready".equals(cookie.getName())).findFirst();
        }

        String requestURI = request.getRequestURI();

        // 使用AntPathMatcher進行路徑匹配
        for (String url : ignoreUrls) {
            if (antPathMatcher.match(contextPath + url, requestURI)) {
                return true;
            }
        }

        if (!ObjectUtils.isEmpty(loginAlready)) {
            return true;
        }
        response.sendRedirect(contextPath +"/emp/toLogin");
        return false;
    }
}
