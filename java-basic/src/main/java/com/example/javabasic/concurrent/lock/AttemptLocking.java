package com.example.javabasic.concurrent.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class AttemptLocking {
    private ReentrantLock lock = new ReentrantLock();

    /**
     * 不等待获取锁，如果未获取到锁，立即返回
     */
    public void untimed() {
        // 尝试获取锁
        boolean captured = lock.tryLock();
        try {
            System.out.println("tryLock(): " + captured);
        } finally {
            // 如果获取到了锁，就释放锁
            if (captured) {
                lock.unlock();
            }
        }
    }

    /**
     * 等待一定时间获取锁
     */
    public void timed() {
        boolean captured = false;
        try {
            // 等待两秒获取锁
            captured = lock.tryLock(2, TimeUnit.SECONDS);
            System.out.println("tryLock(2, TimeUnit.SECONDS): " + captured);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (captured) {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final AttemptLocking al = new AttemptLocking();
        // 刚开始的时候，都可以获取锁
        al.untimed();
        al.timed();
        // 使用匿名线程提前占用锁
        new Thread() {
            { setDaemon(true); }
            public void run() {
                al.lock.lock();
                System.out.println("acquired");
            }
        }.start();
        // 主线程休眠2s，让副线程获取到锁
        Thread.sleep(2000);
        // 这样主线程就阻塞了
        al.untimed();
        al.timed();
    }

}
