package com.example.javabasic.concurrent.resource;

import java.util.Random;

public class Count {
    private int count = 0;
    private Random rand = new Random(45);
    // 增加该值
    public synchronized int increment() {
        int temp = count;
        if (rand.nextBoolean()) {
            Thread.yield();
        }
        return (count = ++temp);
    }
    public synchronized int value() { return count; }
}
