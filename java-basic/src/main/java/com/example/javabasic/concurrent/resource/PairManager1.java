package com.example.javabasic.concurrent.resource;

public class PairManager1 extends PairManager{

    /**
     * 使用synchronized关键字修饰的同步方法
     */
    @Override
    public synchronized void increment() {
        p.incrementX();
        p.incrementY();
        store(getPair());
    }
}
