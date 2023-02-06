package com.example.springtask.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class DemoJob {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final AtomicInteger counts = new AtomicInteger();

    /**
     * 使用@Scheduled注解指定任务执行频率
     */
    @Scheduled(fixedRate = 2000)
    public void execute() {
        logger.info("[execute][定时第 ({}) 次执行]", counts.incrementAndGet());
        // 如果执行次数大于5次，则停止执行,但是这里并不会停止，只是设置了一个标志
        if (counts.get() > 5) {
            System.out.println(Thread.interrupted());
        }
    }

}
