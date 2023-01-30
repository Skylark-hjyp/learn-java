package com.example.javabasic.concurrent.task;

import com.example.javabasic.concurrent.resource.ThreadLocalVariableHolder;

public class Accessor implements Runnable {
    private final int id;

    public Accessor(int id) { this.id = id; }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            // 给当前线程对应的计数 +1
            ThreadLocalVariableHolder.increment();
            System.out.println(this);
            // 让其他线程执行
            Thread.yield();
        }
    }

    public String toString() {
        return "#" + id + ": " + ThreadLocalVariableHolder.get();
    }
}
