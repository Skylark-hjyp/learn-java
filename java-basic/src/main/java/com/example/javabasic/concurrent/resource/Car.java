package com.example.javabasic.concurrent.resource;

public class Car {
    // 当为false时说明已经抛好光，可以打蜡；
    // 当为true时说明已经打好蜡，可以抛光；
    private boolean waxOn = false;

    public synchronized void waxed() {
        waxOn = true;
        notifyAll();
    }
    public synchronized void buffed() {
        waxOn = false;
        notifyAll();
    }

    public synchronized void waitForWaxing() throws InterruptedException {
        // 需要一直循环
        while (waxOn == false) {
            wait();
        }
    }

    public synchronized void waitForBuffing() throws InterruptedException {
        while (waxOn == true) {
            wait();
        }
    }
}
