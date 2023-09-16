package com.example.basic.concurrence.thread;


import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicLong;

public class CompareAndSwap {
    public volatile int value; //值
    //不安全类
    // private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final Unsafe unsafe = getUnsafe();
    //value 的内存偏移（相对与对象头部的偏移，不是绝对偏移）
    private static final long valueOffset;
    //统计失败的次数
    public static final AtomicLong failure = new AtomicLong(0);
    static
    {
        try
        {
            //取得value属性的内存偏移
            valueOffset = unsafe.objectFieldOffset(CompareAndSwap.class.getDeclaredField("value"));
            System.out.println("valueOffset:=" + valueOffset);
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }
    //通过CAS原子操作，进行“比较并交换”
    public final boolean unSafeCompareAndSet(int oldValue, int newValue)
    {
        //原子操作：使用unsafe的“比较并交换”方法进行value属性的交换
        return unsafe.compareAndSwapInt( this, valueOffset, oldValue, newValue );
    }
    //使用无锁编程实现安全的自增方法
    public void selfPlus()
    {
        int oldValue = value;
        //通过CAS原子操作，如果操作失败就自旋，直到操作成功
        for(;;) {
            oldValue = value;
            failure.incrementAndGet();
            if (unSafeCompareAndSet(oldValue, oldValue + 1)) return;
        }
        // do
        // {
        //     // 获取旧值
        //     oldValue = value;
        //     //统计无效的自旋次数
        //     //记录失败的次数
        //     failure.incrementAndGet();
        // } while (!unSafeCompareAndSet(oldValue, oldValue + 1));
    }

    /**
     * 通过反射获取Unsafe
     * @return
     */
    public static Unsafe getUnsafe()
    {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            return (Unsafe) theUnsafe.get(null);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}
