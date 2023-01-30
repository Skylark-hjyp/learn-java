package com.example.javabasic.concurrent.task;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Horse implements Runnable {
    // 全局马的个数计数器
    private static int counter = 0;

    private static Random rand = new Random(45);
    // 公共的并发屏障
    private static CyclicBarrier barrier;
    // 构造器
    public Horse(CyclicBarrier b) { barrier = b; }
    // 马的id
    private final int id = counter ++;
    // 当前马已经走的步数
    private int strides = 0;

    public synchronized int getStrides() { return  strides; }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized (this) {
                    // 让马随机向前移动
                    strides += rand.nextInt();
                }
                // 挂起该线程，等待其他马到达
                barrier.await();
            }
        } catch (InterruptedException e) {

        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        return "Horse " + id + " ";
    }

    public String tracks() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < getStrides(); i ++) {
            s.append("*");
        }
        s.append(id);
        return s.toString();
    }
}
