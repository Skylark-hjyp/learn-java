package com.example.javabasic.concurrent.task;

import java.util.concurrent.Callable;

/**
 * 带返回值的线程
 */
public class TaskWithResult implements Callable {

    private int id;

    public TaskWithResult(int id) {
        this.id = id;
    }

    @Override
    public String call() throws Exception {
        return "result of TaskWithResult " + id;
    }
}
