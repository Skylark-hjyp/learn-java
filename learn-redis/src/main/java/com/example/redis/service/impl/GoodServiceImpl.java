package com.example.redis.service.impl;

import com.example.redis.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class GoodServiceImpl implements GoodService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public Boolean secKillByRedis(Integer goodId) {
        String key = "good-" + goodId;
        Integer res = (Integer) redisTemplate.opsForValue().get(key);
        if (res == null) {
            redisTemplate.opsForValue().set(key, 1000);
            res = (Integer) redisTemplate.opsForValue().get(key);
        }
        if (res >= 0) {
            Long count = redisTemplate.opsForValue().decrement(key);
            return count >= 0;
        }
        return false;
    }
}
