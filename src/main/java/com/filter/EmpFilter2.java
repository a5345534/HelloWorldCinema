package com.filter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class EmpFilter2 implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        Cookie[] cookies = request.getCookies();
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();

        String[] ignoreUrls = { "/emp/toLogin", "/emp/doLogin", "/emp/resetPasswordLink"};
        for (String url : ignoreUrls) {
            if (requestURI.equals(contextPath + url)) {
                return true;
            }
        }
        String[] ignoreUrls2 = { "/front_end/", "/rental/f/", "/mem/f/" };
        for (String url : ignoreUrls2) {
            if (requestURI.startsWith(contextPath + url)) {
                return true;
            }
        }

        if (requestURI.startsWith(contextPath + "/emp/") || requestURI.startsWith(contextPath + "/rental/")
                || requestURI.startsWith(contextPath + "/mem/")) {

            boolean isLoggedIn = false;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("loginAlready")) {
                        isLoggedIn = true;
                        break;
                    }
                }
            }

            if (!isLoggedIn) {
                response.sendRedirect(contextPath + "/emp/toLogin");
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) throws Exception {

    }
}
