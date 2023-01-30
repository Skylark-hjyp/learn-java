package com.example.javabasic.concurrent.task;

import com.example.javabasic.concurrent.resource.Count;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Entrance implements Runnable {

    // 公共资源Count, 记录所有门经过的人的总数
    private static Count count =  new Count();
    // 存储所有的门口数据
    private static List<Entrance> entranceList = new ArrayList<>();
    // 公共资源，是否闭园
    private static volatile boolean canceled = false;
    // 存储该门的数据
    private int number = 0;
    // 每个门独有的id
    private final int id;

    public static void cancel() { canceled = true; }

    public Entrance(int id) {
        this.id = id;
        entranceList.add(this);
    }

    @Override
    public void run() {
        while(!canceled) {
            synchronized (this) {
                ++number;
            }
            System.out.println(this + " Total: " + count.increment());
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("sleep interrupted");
            }
        }
        System.out.println("Stopping" + this);
    }

    public synchronized int getValue() { return number; }

    public String toString() {
        return "Entrance " + id + ": " + getValue();
    }

    /**
     * 直接返回唯一的总数
     * @return
     */
    public static int getTotalCount() {
        return count.value();
    }

    /**
     * 通过遍历所有门经过人统计总数
     * @return
     */
    public static int sumEntrances () {
        int sum = 0;
        for (Entrance entrance : entranceList) {
            sum += entrance.getValue();
        }
        return sum;
    }

    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 2; i ++) {
            exec.execute(new Entrance(i));
        }
        TimeUnit.SECONDS.sleep(3);
        Entrance.cancel();
        exec.shutdown();
        if (!exec.awaitTermination(250, TimeUnit.MILLISECONDS))
            System.out.println("Some tasks were not terminated!");
        System.out.println("Total: " + Entrance.getTotalCount());
        System.out.println("Sum of Entrances: " + Entrance.sumEntrances());
    }
}
