package com.example.javabasic.concurrent.resource;

public class Example {
    private Integer value1 = 0;

    private Integer value2 = 0;

    public synchronized void addValue1() {
        this.value1++;
        System.out.println(Thread.currentThread().getId() + "-增加value1");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void addValue2() {
        this.value2++;
        System.out.println(Thread.currentThread().getId() + "-增加value2");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
