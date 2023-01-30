package com.example.javabasic.concurrent;

import com.example.javabasic.concurrent.resource.Car;
import com.example.javabasic.concurrent.task.WaxOff;
import com.example.javabasic.concurrent.task.WaxOn;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WaxOMatic {
    public static void main(String[] args) throws Exception {
        Car car = new Car();
        ExecutorService exec = Executors.newCachedThreadPool();
        // 启动抛光线程
        exec.execute(new WaxOff(car));
        // 启动打蜡线程
        exec.execute(new WaxOn(car));
        // 睡眠main主线程，让其余两个子线程执行一会
        TimeUnit.SECONDS.sleep(5);
        // 关闭所有线程
        exec.shutdownNow();
    }
}
