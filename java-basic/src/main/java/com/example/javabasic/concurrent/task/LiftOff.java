package com.example.javabasic.concurrent.task;

import java.io.IOException;

/**
 * 线程类，实现Runnable的run方法
 */
public class LiftOff implements Runnable {
    protected int countDown = 10;

    private int id = 0;

    public LiftOff() {}

    public LiftOff(int countDown, int id) {
        this.countDown = countDown;
        this.id = id;
    }

    public String status() {
        return "#" + id + "(" + (countDown > 0 ? countDown : "Liftoff!") + "), ";
    }

    /**
     * 定义run方法
     */
    @Override
    public void run() {
        while (countDown-- > 0) {
            // 输出当前的countDown
            System.out.println(status());
            // 请求线程调度
            // Thread.yield();
            throw new RuntimeException();
        }
    }
}
