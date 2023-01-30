package com.example.javabasic.concurrent.task;

import com.example.javabasic.concurrent.Restaurant;
import com.example.javabasic.concurrent.resource.Meal;

import java.util.concurrent.TimeUnit;

public class Chef implements Runnable {

    private Restaurant restaurant;

    private int count = 0;

    public Chef(Restaurant r) { restaurant = r; }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized (this) {
                    // 如果meal不为空，则一直等待
                    while (restaurant.meal != null) {
                        wait();
                    }
                }
                if (++count == 10) {
                    System.out.println("Out of food, closing!");
                    restaurant.exec.shutdownNow();
                }
                System.out.println("Order up!");
                synchronized (restaurant.waitPerson) {
                    restaurant.meal = new Meal(count);
                    // 激活在waitPerson上等待的线程
                    restaurant.waitPerson.notifyAll();
                }
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch(InterruptedException e) {
            System.out.println("Chef interrupted");
        }
    }
}
