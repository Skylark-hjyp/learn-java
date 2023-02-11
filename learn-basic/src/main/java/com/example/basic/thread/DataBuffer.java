package com.example.basic.thread;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生产者消费者队列
 */
//数据缓冲区，类定义
public class DataBuffer<T> {
    ReentrantLock reentrantLock = new ReentrantLock();
    public static final int MAX_AMOUNT = 10; //数据缓冲区最大长度
    //保存数据
    private final List<T> dataList = new LinkedList<>();
    //数据缓冲区长度
    private Integer amount = 0;
    // 用来保证只有一个线程存元素或者取元素
    private final Object LOCK_OBJECT = new Object();
    // 当队列满了后，用于阻塞生产者
    private final Object NOT_FULL = new Object();
    // 当队列为空时，用于阻塞消费者
    private final Object NOT_EMPTY = new Object();
    // 向数据区增加一个元素
    public void add(T element) throws Exception
    {
        // 队列已满，不能存元素
        while (amount > MAX_AMOUNT)
        {
            synchronized (NOT_FULL)
            {
                System.out.println("队列已经满了！");
                // 等待未满通知
                NOT_FULL.wait();
            }
        }
        // 保证原子性
        synchronized (LOCK_OBJECT)
        {
            dataList.add(element);
            amount++;
            System.out.println(Thread.currentThread().getName() + "生产了一条消息" + amount);
        }
        synchronized (NOT_EMPTY)
        {
            //发送未空通知
            NOT_EMPTY.notify();
        }
    }
    /**
     * 从数据区取出一个商品
     */
    public T fetch() throws Exception
    {
        // 数量为零，不能取元素
        while (amount <= 0)
        {
            synchronized (NOT_EMPTY)
            {
                System.out.println(Thread.currentThread().getName() + "队列已经空了！");
                //等待未空通知
                NOT_EMPTY.wait();
            }
        }
        T element = null;
        // 保证原子性
        synchronized (LOCK_OBJECT)
        {
            element = dataList.remove(0);
            amount--;
            System.out.println(Thread.currentThread().getName() + "消费了一条消息" + amount);
        }
        synchronized (NOT_FULL)
        {
            //发送未满通知
            NOT_FULL.notify();
        }
        return element;
    }
}
