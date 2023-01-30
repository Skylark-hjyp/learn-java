package com.example.javabasic.concurrent;

import com.example.javabasic.concurrent.factory.DaemonThreadFactory;
import com.example.javabasic.concurrent.factory.HandlerThreadFactory;
import com.example.javabasic.concurrent.task.LiftOff;
import com.example.javabasic.concurrent.task.TaskWithResult;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 使用线程池执行任务，不需要显式地创建Thread对象
 * 这样就不会一直反复的创建线程，节省了创建线程的开销，提高了运行速度
 */
public class CacheThreadPool {
    public static void main(String[] args) {
        CacheThreadPool cacheThreadPool = new CacheThreadPool();

        // cacheThreadPool.execWithThreadPool();

        // cacheThreadPool.execThreadWithResult();

        cacheThreadPool.execCustomExceptionHandler();
    }


    /**
     * 使用线程池执行任务
     */
    public void execWithThreadPool() {
        // 创建指定的线程池
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 2; i ++) {
            exec.execute(new LiftOff());
        }
        // 关闭线程池
        exec.shutdown();
    }

    /**
     * 执行带返回值的线程
     * 执行线程时需要exec.submit()
     * 接收返回值时要使用Future<Object>接收
     */
    public void execThreadWithResult() {
        ExecutorService exec = Executors.newCachedThreadPool();
        ArrayList<Future<String>> results = new ArrayList<>();
        for (int i = 0; i < 3; i ++) {
            results.add(exec.submit(new TaskWithResult(i)));
        }
        for (Future<String> fs : results) {
            try {
                System.out.println(fs.get());
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                exec.shutdown();
            }
        }
    }

    /**
     * 向 Executors 传入 自定义线程创建类
     */
    public void execCustomThreadFactory() {
        // 创建指定的线程池，传入自定义的线程创建类
        ExecutorService exec = Executors.newCachedThreadPool(new DaemonThreadFactory());
        for (int i = 0; i < 2; i ++) {
            exec.execute(new LiftOff());
        }
        // 关闭线程池
        exec.shutdown();
    }

    /**
     * 向 Executors 传入自定义异常处理器
     */
    public void execCustomExceptionHandler() {
        // 传入的ThreadFactory在创建线程时会附着一个异常处理类
        ExecutorService exec = Executors.newCachedThreadPool(new HandlerThreadFactory());
        for (int i = 0; i < 2; i ++) {
            exec.execute(new LiftOff());
        }
        // 关闭线程池
        exec.shutdown();
    }

}
