package com.filter;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter({"/mem/f/*","/rental/f/*"})
public class MemFilter implements Filter {
	 @Override
	    public void init(FilterConfig filterConfig) throws ServletException {
	       
	    }

	    @Override
	    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
	        HttpServletRequest httpReq = (HttpServletRequest) req;
	        HttpServletResponse httpRes = (HttpServletResponse) res;
	        HttpSession session = httpReq.getSession(false);

	       
	        String requestURI = httpReq.getRequestURI();
	        
	        if (requestURI.equals(httpReq.getContextPath() + "/mem/login") ||
	            requestURI.equals(httpReq.getContextPath() + "/mem/signup")) {
	            chain.doFilter(req, res); 
	            return;
	        }
	        
	        if (session != null && session.getAttribute("loginSuccess") != null) {
	            
	            chain.doFilter(req, res);
	        } else {
	        	 String message = URLEncoder.encode("請登入會員", StandardCharsets.UTF_8.toString());
	             httpRes.sendRedirect(httpReq.getContextPath() + "/mem/login?message=" + message);
	        }
	    }

	    @Override
	    public void destroy() {
	     
	    }
}
