package com.example.javabasic.concurrent.resource;

import com.example.javabasic.concurrent.task.Accessor;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadLocalVariableHolder {
    private static ThreadLocal<Integer> value = new ThreadLocal<>() {
        private Random rand = new Random(45);
        protected synchronized Integer initialValue() {
            return rand.nextInt(10000);
        }
    };

    // 给对应线程的计数器加一
    public static void increment() {
        value.set(value.get() + 1);
    }

    public static int get() {return value.get();}

    public static void main(String[] args) throws Exception{
        // 创建线程池
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 2; i ++) {
            exec.execute(new Accessor(i));
        }
        // 主线程休眠，让其他线程执行会
        TimeUnit.SECONDS.sleep(3);

        exec.shutdown();

    }
}
