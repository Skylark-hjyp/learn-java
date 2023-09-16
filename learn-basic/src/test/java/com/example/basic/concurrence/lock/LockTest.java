package com.example.basic.concurrence.lock;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {
    private int count;

    @Test
    public void testReentrantLock() throws InterruptedException {
        Lock lock = new ReentrantLock();

        ExecutorService executorService = Executors.newCachedThreadPool();

        CountDownLatch countDownLatch = new CountDownLatch(3);

        for (int i = 0; i < 3; i ++) {
            executorService.execute(() -> {
                for (int j = 0; j < 100; j ++) {
                    // 获取锁
                    lock.lock();
                    try {
                        count ++;
                    } finally {
                        // 释放锁
                        lock.unlock();
                    }
                }
                // 每完成一个线程，就更新countDownLatch
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();

        System.out.println(count);

    }

}
