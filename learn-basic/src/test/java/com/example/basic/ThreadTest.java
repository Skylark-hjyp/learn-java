package com.example.basic;

import com.example.basic.entity.Count;
import com.example.basic.thread.CompareAndSwap;
import com.example.basic.thread.DataBuffer;
import com.example.basic.thread.MyThread;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadTest {
    @Test
    public void testCreateThread() throws InterruptedException {
        Thread thread = null;
        //方法一：使用Thread子类创建和启动线程
        for (int i = 0; i < 2; i++) {
            thread = new MyThread();
            thread.start();
        }
        Thread.sleep(3000);
        System.out.println(Thread.currentThread().getName() + " 运行结束.");
    }

    @Test
    public void testThread2() throws InterruptedException {
        // Thread thread = new Thread(new Thread2(), "线程2");
        Thread thread = new Thread(() -> System.out.println("我是匿名实现类"));
        thread.start();
        Thread.sleep(1000);
    }

    @Test
    public void testThread3() throws ExecutionException, InterruptedException {
        // 将Callable接口实例传入FutureTask类
        FutureTask<Long> futureTask = new FutureTask<>(() -> {
            long startTime = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + " 线程运行开始.");
            Thread.sleep(1000);
            for (int i = 0; i < 200; i++) {
                int j = i * 10000;
            }
            long used = System.currentTimeMillis() - startTime;
            return used;
        });
        Thread thread = new Thread(futureTask);
        thread.start();
        // 等子线程执行完毕后返回
        Long res = futureTask.get();
        System.out.println(res);
    }

    @Test
    public void testExecutor() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        // 通过execute方法向线程池提交一个runnable接口匿名类，不带返回值
        executorService.execute(() -> {
            System.out.println("我是Runnable接口线程");
        });
        Future<Integer> future = executorService.submit(() -> {
            System.out.println("我是Callable或者Runnable接口线程");
            return 2;
        });
        // 获取返回结果
        System.out.println(future.get());
    }

    @Test
    public void testExecutor2() {
        ThreadLocal<Integer> name = new ThreadLocal<>();
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i ++) {
            int finalI = i;
            executorService.execute(() -> {
                name.set(finalI);
                System.out.println(Thread.currentThread().getName() + "--" + name.get());
            });
        }
    }

    /**
     * 测试生产者消费者队列
     * @throws InterruptedException
     */
    @Test
    public void testProducerConsumerQueue() throws InterruptedException {
        //共享数据区，实例对象
        DataBuffer<String> dataBuffer = new DataBuffer<>();
        // 同时并发执行的线程数
        final int THREAD_TOTAL = 20;
        //线程池，用于多线程模拟测试
        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_TOTAL);
        //假定共11条线程，其中有10个消费者，但是只有1个生产者
        final int CONSUMER_TOTAL = 10;
        final int PRODUCE_TOTAL = 1;
        for (int i = 0; i < PRODUCE_TOTAL; i++) {
            //生产者线程每生产一个商品，间隔50毫秒
            threadPool.submit(() -> {
                for (int j = 0; j < 10; j ++) {
                    //首先生成一个随机的商品
                    String s = "商品";
                    //将商品加上共享数据区
                    try {
                        dataBuffer.add(s);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        for (int i = 0; i < CONSUMER_TOTAL; i++)
        {
            //消费者线程每消费一个商品，间隔100毫秒
            threadPool.submit(() -> {
                for (int j = 0; j < 2; j ++) {
                    // 从PetStore获取商品
                    String s = null;
                    try {
                        s = dataBuffer.fetch();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        Thread.sleep(10000);
    }

    /**
     * 测试CAS操作
     * @throws InterruptedException
     */
    @Test
    public void testCAS() throws InterruptedException {
        final CompareAndSwap compareAndSwap = new CompareAndSwap();
        AtomicInteger res = new AtomicInteger(0);
        //倒数闩，需要倒数THREAD_COUNT次
        CountDownLatch latch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++)
        {
            // 提交10个任务
            Executors.newCachedThreadPool().submit(() ->
            {
                //每个任务累加1000次
                for (int j = 0; j < 1000; j++)
                {
                    compareAndSwap.selfPlus();
                    res.getAndIncrement();
                }
                latch.countDown();// 执行完一个任务，倒数闩减少一次
            });
        }
        latch.await();// 主线程等待倒数闩倒数完毕
        System.out.println(res);
        System.out.println("累加之和：" + compareAndSwap.value);
        System.out.println("失败次数：" + CompareAndSwap.failure.get());
    }

    /**
     * 测试可重入显式锁
     */
    @Test
    public void testLock() throws InterruptedException {
        Count count = new Count();
        Lock lock = new ReentrantLock();
        ExecutorService executorService = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(5);
        for (int i = 0; i < 5; i ++) {
            executorService.execute(() -> {
                // 获取锁，必须在try块外
                lock.lock();
                try {
                    for (int j = 0; j < 1000; j++) {
                        count.add();
                        if (j % 200 == 0) {
                            System.out.println(Thread.currentThread().getName());
                        }
                    }
                } finally {
                    // 释放锁，在finally块中
                    lock.unlock();
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        System.out.println(count.number);
    }
}
