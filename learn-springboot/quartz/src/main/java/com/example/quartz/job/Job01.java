package com.example.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 继承 QuartzJobBean 抽象类
 * */
@Slf4j
public class Job01 extends QuartzJobBean {
    private AtomicInteger counts = new AtomicInteger();
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        // 执行这里的时候会发现总是第1次执行，说明每次执行的Job都是新建的
        log.info("[executeInternal][定时任务1第 ({}) 次执行]", counts.incrementAndGet());
    }
}
