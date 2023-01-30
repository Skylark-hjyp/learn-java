package com.example.javabasic.concurrent.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class PairManager {
    AtomicInteger checkCounter = new AtomicInteger(0);

    // 保存的Pair
    protected Pair p = new Pair();

    // 存储所有Pair的容器
    private List<Pair> storage = Collections.synchronizedList(new ArrayList<>());

    /**
     * 获取当前保存的Pair
     * @return
     */
    public synchronized Pair getPair() {
        return new Pair(p.getX(), p.getY());
    }

    /**
     * 向storage里加入Pair
     * @param p
     */
    protected void store(Pair p) {
        storage.add(p);
        try {
            TimeUnit.MILLISECONDS.sleep(50);
        } catch (InterruptedException ignore) {}
    }

    /**
     * x和 y自增代码
     */
    public abstract void increment();
}
