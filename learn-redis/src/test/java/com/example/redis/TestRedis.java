package com.example.redis;

import com.example.redis.service.GoodService;
import org.junit.jupiter.api.Assertions;
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
    public void testSecKillByRedisFun1() {
        Assertions.assertTrue(goodService.secKillByRedisFun1(1));
    }

    @Test
    public void testSecKillByRedisFun2() {
        Assertions.assertTrue(goodService.secKillByRedisFun2(1));
    }

    @Test
    public void testSecKillByRedisFun3() {
        Assertions.assertTrue(goodService.secKillByRedisFun3(1));
    }

    @Test
    public void testQueryGoodCount() {
        Integer res = goodService.queryGoodCount("good-stock-1");
        System.out.println(res);
    }

}
