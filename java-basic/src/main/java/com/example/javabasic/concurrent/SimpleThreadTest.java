package com.example.javabasic.concurrent;

import com.example.javabasic.concurrent.task.Joiner;
import com.example.javabasic.concurrent.task.LiftOff;
import com.example.javabasic.concurrent.task.Sleeper;

public class SimpleThreadTest {

    public static void main(String[] args) {
        SimpleThreadTest simpleThreadTest = new SimpleThreadTest();

        simpleThreadTest.join();
    }

    /**
     * 通过实例化Thread类启动两个线程
     */
    public void example() {
        // 启动两个线程
        for (int i = 0; i < 2; i ++) {
            // 通过创建Thread类来启动线程
            new Thread(new LiftOff(5, i)).start();
        }
        // 可以看到这句话立马被输出了，这是因为任务由别的线程执行，不占用当前主线程
        System.out.println("Waiting for LiftOff");
    }

    /**
     * 使用join()函数中途加入一个线程
     * 主线程只能等待加入线程执行完毕后才会继续执行
     */
    public void join() {
        Sleeper sleeper = new Sleeper("中途加入的线程", 2000);
        Joiner join = new Joiner("刚开始执行的线程", sleeper);

    }
}
