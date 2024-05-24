package com.websocket;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final Map<String, UserDto> sessionMap = new ConcurrentHashMap<>();

    public MyWebSocketHandler(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        String uri = webSocketSession.getUri().toString();
        String[] urlParts = uri.split("/");
        String UserName = urlParts[urlParts.length - 1];
        String JSESSIONID = getJSESSIONID(webSocketSession);
        System.out.println("Connected: " + JSESSIONID);
        UserDto user = new UserDto(webSocketSession, UserName);
        sessionMap.put(webSocketSession.getId(), user);
        Map<String, String> userMap = new HashMap<>();
        userMap.put(user.webSocketSession.getId(), user.username);

        redisTemplate.opsForHash().putAll("connections:" + JSESSIONID, userMap);

        // 檢查Redis中是否存在此sessionId的記錄
        boolean sessionExists = redisTemplate.hasKey("connections:" + JSESSIONID);
        loadAndSendChatHistory(webSocketSession, JSESSIONID);
        if (!sessionExists) {
            // 若不存在，創建新的記錄並存儲session信息
            String message = "Welcome to the WebSocket server!";
            notifyAllSessions(JSESSIONID, new TextMessage(message));
//            redisTemplate.opsForList().rightPush("chat_history:" + JSESSIONID, message); // 初始化聊天記錄
        } else {
            String message = user.username + " is come";
            notifyAllSessions(JSESSIONID, new TextMessage(message));
//            redisTemplate.opsForList().rightPush("chat_history:" + JSESSIONID, message);
        }

    }

    private static String getJSESSIONID(WebSocketSession webSocketSession) {
        String uri = webSocketSession.getUri().toString();
        String[] urlParts = uri.split("/");
        String JSESSIONID = urlParts[urlParts.length - 2];
        if (JSESSIONID.equals("newConnect")) {
            HttpSession httpSession = (HttpSession) webSocketSession.getAttributes().get("httpSession");
            JSESSIONID = httpSession.getId();
        }
        return JSESSIONID;
    }

    private void loadAndSendChatHistory(WebSocketSession webSocketSession, String JSESSIONID) throws IOException {
        List<Object> chatHistory = redisTemplate.opsForList().range("chat_history:" + JSESSIONID, 0, -1);
        if (chatHistory != null) {
            for (Object chatMessage : chatHistory) {
                webSocketSession.sendMessage(new TextMessage(chatMessage.toString()));
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession webSocketSession, TextMessage message) throws Exception {
        String JSESSIONID = getJSESSIONID(webSocketSession);
        UserDto userDto = sessionMap.get(webSocketSession.getId());
        String chatMessage = userDto.username + ":" + message.getPayload();
        redisTemplate.opsForList().rightPush("chat_history:" + JSESSIONID, chatMessage);
        notifyAllSessions(JSESSIONID, new TextMessage(chatMessage));
    }


    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, org.springframework.web.socket.CloseStatus status) throws Exception {
        String JSESSIONID = getJSESSIONID(webSocketSession);
        System.out.println("Disconnected: " + JSESSIONID);

        UserDto userDto = sessionMap.get(webSocketSession.getId());
        String message = userDto.username + " is leaved";
        notifyAllSessions(JSESSIONID, new TextMessage(message));
//        redisTemplate.opsForList().rightPush("chat_history:" + JSESSIONID, message);
    }

    //    private static String getJSESSIONID(WebSocketSession webSocketSession) {
//        HttpSession httpSession = (HttpSession) webSocketSession.getAttributes().get("httpSession");
//        return httpSession.getId();
//    }
    private void notifyAllSessions(String JSESSIONID, TextMessage message) throws Exception {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries("connections:" + JSESSIONID);

        for (Object webSocketSessionId : entries.keySet()) {
            String key = (String) webSocketSessionId;
            UserDto userDto = sessionMap.get(key);
            if (!ObjectUtils.isEmpty(userDto)) {
                if (userDto.webSocketSession.isOpen()) {
                    userDto.webSocketSession.sendMessage(message);
                }
            }
        }
    }
}
