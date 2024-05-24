package com.filter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Component
public class CustomHandshakeInterceptor implements HandshakeInterceptor {


    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof org.springframework.http.server.ServletServerHttpRequest) {
            org.springframework.http.server.ServletServerHttpRequest servletRequest = (org.springframework.http.server.ServletServerHttpRequest) request;
            HttpSession httpSession = servletRequest.getServletRequest().getSession();
            attributes.put("httpSession", httpSession);
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
//        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
//        String id = servletRequest.getSession().getId();
//        String sessionId = request.getHeaders().getFirst("Sec-WebSocket-Key");
//        if (sessionId != null) {
////            response.getHeaders().add(HttpHeaders.SET_COOKIE, "sessionId=" + sessionId + "; Path=/; HttpOnly; SameSite=None; Secure");
//            response.getHeaders().add(HttpHeaders.SET_COOKIE, "sessionId=" + sessionId + "; Path=/");
//        }
    }
}
