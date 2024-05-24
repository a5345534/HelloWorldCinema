package com.websocket;

import lombok.AllArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.io.Serializable;

@AllArgsConstructor
public class UserDto implements Serializable {
    public WebSocketSession webSocketSession;
    public String username;
}
