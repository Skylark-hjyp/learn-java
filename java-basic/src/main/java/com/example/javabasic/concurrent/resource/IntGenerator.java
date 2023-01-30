package com.example.javabasic.concurrent.resource;

public abstract class IntGenerator {
    private volatile boolean canceled = false;

    /**
     * 产生下一个偶数
     * @return
     */
    public abstract int next();

    /**
     * 停止所有的线程
     */
    public void cancel() {
        canceled = true;
    }

    public boolean isCanceled() {
        return canceled;
    }
}
