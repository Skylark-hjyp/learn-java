package com.example.javabasic.concurrent.task;

import com.example.javabasic.concurrent.resource.Car;

import java.util.concurrent.TimeUnit;

public class WaxOn implements Runnable {
    private Car car;

    public WaxOn(Car c) { car = c; }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                // 判断抛光任务是否完成
                car.waitForBuffing();
                System.out.println("Wax on ! ");
                // 模拟抛光时间
                TimeUnit.MILLISECONDS.sleep(200);
                // 设定车的状态为打蜡好
                car.waxed();
            }
        } catch (InterruptedException e) {
            System.out.println("Exiting via interrupt ");
        }
        System.out.println("Ending Wax On task");
    }
}
