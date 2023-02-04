package com.example.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class TestRedis {
    @Autowired
    private StringRedisTemplate  stringRedisTemplate;

    @Test
    public void testAdd() {
        stringRedisTemplate.opsForValue().set("name", "jyp");
    }

}
