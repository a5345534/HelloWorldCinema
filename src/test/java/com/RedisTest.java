package com;

import com.websocket.MyWebSocketHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.stream.Stream;

@SpringBootTest
public class RedisTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void deleteRedisRecord() {
        String sessionId = "28F3F429FF277055AE95CC8145D2852F";
        System.out.println("Disconnected: " + sessionId);
        redisTemplate.delete("connections:" + sessionId);
        // 可以選擇在斷開連接時刪除聊天記錄
        redisTemplate.delete("chat_history:" + sessionId);
    }
    @Test
    public void clearRedisRecord() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }
}
