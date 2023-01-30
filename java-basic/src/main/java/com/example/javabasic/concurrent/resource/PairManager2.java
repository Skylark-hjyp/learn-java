package com.example.javabasic.concurrent.resource;

public class PairManager2 extends PairManager{
    @Override
    public void increment() {
        Pair temp;
        synchronized (this) {
            p.incrementY();
            p.incrementX();
            temp = getPair();
        }
        store(temp);
    }
}
