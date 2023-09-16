package com.example.basic.concurrence.thread;

public class Thread2 implements Runnable{
    @Override
    public void run() {
        System.out.println("我是通过实现Runnable接口创建的线程");
    }
}
