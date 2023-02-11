package com.example.basic.thread;

public class MyThread extends Thread{
    //线程的编号
    static int threadNo = 1;

    public MyThread() {
        super("DemoThread-" + threadNo++);
    }

    public void run() {
        for (int i = 1; i < 2; i++) {
            System.out.println(getName() + "轮次" + i);
        }
        System.out.println(getName() + "运行结束");
    }
}
