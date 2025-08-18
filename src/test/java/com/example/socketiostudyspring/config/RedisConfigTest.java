package com.example.socketiostudyspring.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class RedisConfigTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testRedis() {
        redisTemplate.opsForValue().set("newKey", "newValue");
        String value = (String) redisTemplate.opsForValue().get("newKey");
        System.out.println(value);
    }

}