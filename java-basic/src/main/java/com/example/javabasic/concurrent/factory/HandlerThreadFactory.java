package com.example.javabasic.concurrent.factory;

import com.example.javabasic.concurrent.MyUncaughExceptionHandler;

import java.util.concurrent.ThreadFactory;

public class HandlerThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        System.out.println(this + " creating a new thread");
        // 创建线程
        Thread thread = new Thread(r);
        System.out.println("created " + thread);
        // 给线程添加一个异常处理器
        thread.setUncaughtExceptionHandler(new MyUncaughExceptionHandler());
        System.out.println("eh = " + thread.getUncaughtExceptionHandler());
        // 返回线程
        return thread;
    }
}
