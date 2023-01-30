package com.example.javabasic.concurrent.task;


public class Sleeper extends Thread{
    private int duration;

    public Sleeper(String name, int sleepTime) {
        super(name);
        duration = sleepTime;
        // 把start方法在构造函数中调用，这样不需要显式调用start方法
        start();
    }

    public void run() {
        try {
            // 一会要加入的线程，在这里休眠一段时间
            sleep(duration);
        } catch (InterruptedException e) {
            System.out.println(getName() + " was interrupted. " + "isInterrupted(): " + isInterrupted());
            return;
        }
        System.out.println(getName() + "has awakened");
    }
}
