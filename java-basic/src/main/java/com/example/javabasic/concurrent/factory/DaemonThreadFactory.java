package com.example.javabasic.concurrent.factory;

import java.util.concurrent.ThreadFactory;

/**
 * 继承ThreadFactory接口自定义创建线程
 */
public class DaemonThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        // 将线程设置为后台线程
        thread.setDaemon(true);
        return thread;
    }
}
