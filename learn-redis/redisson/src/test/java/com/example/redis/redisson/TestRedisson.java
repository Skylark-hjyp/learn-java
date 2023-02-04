package com.example.redis.redisson;


import org.junit.jupiter.api.Test;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@SpringBootTest
public class TestRedisson {
    private static final String LOCK_KEY = "anylock";

    @Autowired // <1>
    private RedissonClient redissonClient;

    /**
     *  redisson分布式锁
     */
    @Test
    public void testLock() throws InterruptedException {
        // <2.1> 启动一个线程 A ，去占有锁
        new Thread(() -> {
            // 加锁以后 10 秒钟自动解锁
            // 无需调用 unlock 方法手动解锁
            final RLock lock = redissonClient.getLock(LOCK_KEY);
            lock.lock(10, TimeUnit.SECONDS);
        }).start();
        // <2.2> 简单 sleep 1 秒，保证线程 A 成功持有锁
        Thread.sleep(1000L);

        // <3> 尝试加锁，最多等待 100 秒，上锁以后 10 秒自动解锁
        System.out.printf("准备开始获得锁时间：%s%n", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        final RLock lock = redissonClient.getLock(LOCK_KEY);
        boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
        if (res) {
            System.out.printf("实际获得锁时间：%s%n", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        } else {
            System.out.println("加锁失败");
        }
    }


    /**
     * 限流器
     * @throws Exception
     */
    @Test
    public void testRateLimiter() throws Exception{
        // 创建 RRateLimiter 对象
        RRateLimiter rateLimiter = redissonClient.getRateLimiter("myRateLimiter");
        // 初始化：最大流速 = 每 1 秒钟产生 2 个令牌
        rateLimiter.trySetRate(RateType.OVERALL, 2, 1, RateIntervalUnit.SECONDS);
//        rateLimiter.trySetRate(RateType.PER_CLIENT, 50, 1, RateIntervalUnit.MINUTES);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 因为进行了限流，所以后面的循环不会获取到令牌
        for (int i = 0; i < 5; i++) {
            System.out.printf("%s：获得锁结果(%s)%n", simpleDateFormat.format(new Date()),
                    rateLimiter.tryAcquire());
            Thread.sleep(250L);
        }
    }
}
