package com.example.javabasic.concurrent.task;

import com.example.javabasic.concurrent.resource.Car;

import java.util.concurrent.TimeUnit;

public class WaxOff implements Runnable {
    private Car car;

    public WaxOff(Car c) { car =c; }

    public void run() {
        try {
            // 如果线程没有被终止
            while (!Thread.interrupted()) {
                // 判断打蜡任务是否完成，如果没有完成，则一直休眠
                car.waitForWaxing();
                System.out.println("Wax Off!");
                TimeUnit.MILLISECONDS.sleep(200);
                // 设置车的状态为已经抛光
                car.buffed();
            }
        } catch(InterruptedException e) {
            System.out.println("Exiting via interrupt");
        }
        System.out.println("Ending Wax Off task!");
    }
}
