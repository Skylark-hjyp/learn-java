package com.example.redis.service.impl;

import com.example.redis.service.GoodService;
import com.example.redis.utils.RedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class GoodServiceImpl implements GoodService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisLock redisLock;

    /**
     * Redis秒杀方法一：先加分布式锁，然后查询缓存，根据库存量数量进行后续操作：如果库存量大于零，则扣减库存，返回true；否则返回false；
     * @param goodId 商品ID
     * @return 成功返回true，失败返回false
     */
    @Override
    public Boolean secKillByRedisFun1(Integer goodId) {
        // 根据商品ID构造key
        String goodKey = "good-stock-" + goodId;
        String userId = Thread.currentThread().getName() + "-" + System.currentTimeMillis();
        // 使用商品作为锁，锁的粒度较大
        String lockId = "sec-kill-lock-" + goodId;
        return this.subStock(lockId, userId, goodKey);
    }

    /**
     * Redis秒杀方法二：先加分布式锁，然后查询缓存，根据库存量数量进行后续操作：如果库存量大于零，则扣减库存，返回true；否则返回false；
     * 分布式锁细化锁粒度，按库存量作为锁粒度，进一步提升并发量
     * @param goodId 商品ID
     * @return 成功返回true，失败返回false
     */
    @Override
    public Boolean secKillByRedisFun2(Integer goodId) {
        // 根据商品ID构造key
        String goodKey = "good-stock-" + goodId;
        // 查询库存，使用库存量作为锁，细化锁粒度，提高并发量
        Integer curStock = (Integer) redisTemplate.opsForValue().get(goodKey);
        if (curStock <= 0) {
            return false;
        }
        String userId = Thread.currentThread().getName() + "-" + System.currentTimeMillis();
        String lockId = "sec-kill-lock-" + goodId + "-" + curStock;
        return this.subStock(lockId, userId, goodKey);
    }

    /**
     * 使用分布式锁秒杀，加锁后再查询redis库存，最后扣减库存
     * @param lockId 锁ID
     * @param userId 用户ID
     * @param goodKey　商品ID
     * @return 秒杀成功返回 true，否则返回 false
     */
    private boolean subStock(String lockId, String userId, String goodKey) {
        // 尝试先加锁，如果加锁成功再进行查询库存量，和扣减库存操作，此时只能有一个线程进入代码块
        if (redisLock.lock(lockId, userId, 4000)) {
            try {
                // 查询库存
                Integer stock = (Integer) redisTemplate.opsForValue().get(goodKey);
                if (stock == null) {
                    System.out.println("商品不在缓存中");
                }
                // 如果剩余库存量大于零，则扣减库存
                if (stock > 0) {
                    redisTemplate.opsForValue().decrement(goodKey);
                    return true;
                } else {
                    return false;
                }
            } finally {
                // 释放锁
                redisLock.unlock(lockId, userId);
            }
        }
        return false;
    }

    /**
     * Redis 秒杀方案三：使用原子操作DECR和INCR扣减库存
     * @param goodId 商品ID
     * @return
     */
    @Override
    public Boolean secKillByRedisFun3(Integer goodId) {
        // 根据商品ID构造key
        String goodKey = "good-stock-" + goodId;
        Long stockCount = redisTemplate.opsForValue().decrement(goodKey);
        if (stockCount >= 0) {
            return true;
        } else {
            // 如果库存不够，则恢复库存
            redisTemplate.opsForValue().increment(goodKey);
            return false;
        }
    }


    /**
     * 将Redis作为只读缓存，先查询缓存，如果发现没有缓存，则需要去数据库查询，该过程借助分布式锁限制查询数据库线程数量，防止缓存击穿
     * @param goodKey 所要查询的商品key
     * @return 商品对应的库存量
     */
    public Integer queryGoodCount(String goodKey) {
        // 查询缓存
        Integer res = (Integer) redisTemplate.opsForValue().get(goodKey);
        // 如果缓存为空，需要查询数据库，设置缓存
        if (res == null) {
            // 设置分布式锁，查询数据库，保证只有一个线程进入，防止缓存击穿
            String userId = Thread.currentThread().getName() + "-" + System.currentTimeMillis();
            String lockId = "query-lock-" + goodKey;
            // 尝试加锁，如果加锁成功，则设置缓存；如果加锁失败，则说明已经存在缓存
            if (redisLock.lock(lockId, userId, 400)) {
                // 先查询数据库，然后设置Redis缓存
                redisTemplate.opsForValue().set(goodKey, 1000);
            }
            // 查询缓存并返回
            return (Integer) redisTemplate.opsForValue().get(goodKey);
        } else {
            // 缓存若存在，直接返回
            return (Integer) redisTemplate.opsForValue().get(goodKey);
        }
    }

}
