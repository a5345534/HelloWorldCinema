package com.filter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class MemFilter implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession(false);
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		String loginPath = contextPath + "/mem/f/login";
		String signupPath = contextPath + "/mem/signup";

		// Debug 信息
		System.out.println("MemInterceptor - Request URI: " + requestURI);
		System.out.println("MemInterceptor - Context Path: " + contextPath);
		System.out.println("MemInterceptor - Login Path: " + loginPath);
		System.out.println("MemInterceptor - Session: " + session);
		System.out.println("MemInterceptor - LoginSuccess Attribute: "
				+ (session != null ? session.getAttribute("loginSuccess") : "null"));

		if (requestURI.equals(loginPath) || requestURI.equals(signupPath)) {
			return true;
		}

	    if (requestURI.startsWith(contextPath + "/mem/f/m/") || requestURI.startsWith(contextPath + "/rental/f/m/")) {
	        
	        if (session != null && session.getAttribute("loginSuccess") != null) {
	            return true;
	        } else { 
	            String message = URLEncoder.encode("請登入會員", StandardCharsets.UTF_8.toString());
	            response.sendRedirect(loginPath + "?message=" + message);
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
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}
}
