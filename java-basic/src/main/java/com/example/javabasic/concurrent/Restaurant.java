package com.example.javabasic.concurrent;

import com.example.javabasic.concurrent.resource.Meal;
import com.example.javabasic.concurrent.task.Chef;
import com.example.javabasic.concurrent.task.WaitPerson;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Restaurant {
    public Meal meal;
    public ExecutorService exec = Executors.newCachedThreadPool();
    public WaitPerson waitPerson = new WaitPerson(this);
    public Chef chef = new Chef(this);
    public Restaurant() {
        exec.execute(chef);
        exec.execute(waitPerson);
    }

    public static void main(String[] args) {
        new Restaurant();
    }
}
