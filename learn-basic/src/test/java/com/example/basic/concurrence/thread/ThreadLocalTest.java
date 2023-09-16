package com.example.basic.concurrence.thread;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ThreadLocalTest {
    // ThreadLocal 变量，每个线程都有自己的副本
    private static final ThreadLocal<Integer> id = new ThreadLocal<>();

    @Test
    public void testThreadLocal() {
        // 创建线程池
        ExecutorService executorService = Executors.newCachedThreadPool();

        try {
            for (int i = 0; i < 5; i ++) {
                int finalI = i;
                executorService.execute(() -> {
                    // 设置ThreadLocal 变量
                    id.set(finalI);
                    // 获取ThreadLocal变量
                    log.info("The id of thread {} is {}", Thread.currentThread().getName(), id.get());
                });
            }
        } finally {
            id.remove();
            executorService.shutdown();
        }
    }
}
