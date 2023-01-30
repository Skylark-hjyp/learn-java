package com.example.javabasic.concurrent.resource;

import com.example.javabasic.concurrent.task.EvenChecker;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MutexEvenGenerator extends IntGenerator {
    private int currentEvenValue = 0;

    private Lock lock = new ReentrantLock();

    @Override
    public int next() {
        // 加锁
        lock.lock();
        try {
            // 因为自加操作不是原子操作，所以使用volatile 也不能解决问题，必须使用synchronized
            ++currentEvenValue;
            // 线程可能在这里阻塞，从而导致没有看到其他线程的工作
            // 一种情况是：当前线程已经加一，此时阻塞；另一个线程也加一，然后继续执行本线程的另一个加一
            Thread.yield();
            ++currentEvenValue;
            // TODO: 不懂为什么这里返回也得放在锁里
            return currentEvenValue;
        } finally {
            // 释放锁
            lock.unlock();
        }
    }
}
