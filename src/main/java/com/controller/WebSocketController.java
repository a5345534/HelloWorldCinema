package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("websocket")
public class WebSocketController {
    @Autowired
    private RedisTemplate redisTemplate;
    @GetMapping("client_chat")
    public String webSocket(){
        return "websocket/client_chat";
    }
    @GetMapping("server_chat")
    public ModelAndView backend_chat() {
        ModelAndView mav = new ModelAndView("websocket/server_chat");
        Set<String> JSESSIONIDs = redisTemplate.keys("connections:*");
        Map<String, Map<Object, Object>> sessions = new HashMap<>();

        if (JSESSIONIDs != null) {
            for (String JSESSIONID : JSESSIONIDs) {
                Map<Object, Object> connectionInfo = redisTemplate.opsForHash().entries(JSESSIONID);
                String replaceJSESSIONID = JSESSIONID.replace("connections:", "");
                sessions.put(replaceJSESSIONID, connectionInfo);
            }
        }

        mav.addObject("sessions", sessions);
        return mav;
    }
}
