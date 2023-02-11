package com.example.javabasic.concurrent;

public class CreateThread {
    //线程的编号
    static int threadNo = 1;
    static class DemoThread extends Thread {
        public DemoThread() {
            super("DemoThread-" + threadNo++);
        }
        public void run() {
            for (int i = 1; i < 2; i++) {
                System.out.println(getName() + "轮次" + i);
            }
            System.out.println(getName() + "运行结束");
        }
    }
    public static void main(String args[]) throws InterruptedException {
        // Thread thread = null;
        // //方法一：使用Thread子类创建和启动线程
        // for (int i = 0; i < 2; i++) {
        //     thread = new DemoThread();
        //     // 调用start方法，执行内部的run方法
        //     thread.start();
        // }
        System.out.println(Thread.currentThread().getName() + "运行结束");
    }

    public void test() {
        System.out.println("测试方法");
    }
}
