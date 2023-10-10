package com.example.redis.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisLock {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * Redis 分布式锁 【加锁】
     * @param lockId 锁的ID
     * @param userId 当前用户身份
     * @param time 锁过期时间
     * @return 加锁成功返回true，加锁失败返回false
     */
    public boolean lock(String lockId, String userId, long time){
        /**
         * 使用SET lock_key unique_value NX PX 10000 语句加锁并设置超时时间
         * 可以保证原子性，超时自动释放，不会产生死锁
         */
        boolean res = redisTemplate.opsForValue().setIfAbsent(lockId, userId, time, TimeUnit.MILLISECONDS);
        return res;
    }


    /**
     * Redis 分布式锁 【解锁】
     * @param lockId 锁ID
     * @param userId　用户ID
     * @return 解锁成功返回true，否则返回false
     */
    public boolean unlock(String lockId, String userId) {
        // 获取锁，因为需要判断解锁人是否是持锁人，防止被别人解锁
        String realUserId = (String) redisTemplate.opsForValue().get(lockId);
        // 如果查询结果为null，说明key不存在，自然解锁成功
        if(realUserId == null) {
            return true;
        }
        boolean res = false;
        // 如果解锁人为持锁人，则解锁
        if (realUserId.equals(userId)) {
            res = Boolean.TRUE.equals(redisTemplate.delete(lockId));
        }
        return res;
    }
}
