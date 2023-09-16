package com.example.redis;

import com.example.redis.service.GoodService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class TestRedis {
    @Autowired
    private StringRedisTemplate  stringRedisTemplate;

    @Autowired
    private GoodService goodService;

    @Test
    public void testAdd() {
        stringRedisTemplate.opsForValue().set("name", "jyp");
    }

    @Test
    public void testSecKill() {
        goodService.secKillByRedis(1);
    }

}
