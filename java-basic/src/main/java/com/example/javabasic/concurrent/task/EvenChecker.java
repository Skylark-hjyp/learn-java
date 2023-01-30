package com.example.javabasic.concurrent.task;

import com.example.javabasic.concurrent.resource.MutexEvenGenerator;
import com.example.javabasic.concurrent.resource.SynchronizedEvenGenerator;
import com.example.javabasic.concurrent.resource.IntGenerator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EvenChecker implements Runnable{

    private IntGenerator generator;

    private final int id;

    public EvenChecker(IntGenerator g, int id) {
        generator = g;
        this.id = id;
    }

    @Override
    public void run() {
        while (!generator.isCanceled()) {
            // 生成下一个偶数
            int val = generator.next();
            if (val % 2 != 0) {
                System.out.println(val + " not even!");
                // 这样可以取消所有的检查线程
                generator.cancel();
            }
        }
    }

    /**
     * 开启多个线程，检查共享资源
     * @param gp 共享资源
     * @param count 线程数
     */
    public static void test(IntGenerator gp, int count) {
        System.out.println("Please Control-C to exit");
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < count; i ++) {
            // 让新的线程执行任务
            exec.execute(new EvenChecker(gp, i));
        }
        exec.shutdown();
    }

    public static void main(String[] args) {
        // 让三个线程同时检查临界资源
        // EvenChecker.test(new SynchronizedEvenGenerator(), 3);
        EvenChecker.test(new MutexEvenGenerator(), 3);
    }
}
