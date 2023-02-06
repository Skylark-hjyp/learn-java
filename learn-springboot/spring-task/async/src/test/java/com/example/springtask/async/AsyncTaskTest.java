package com.example.springtask.async;

import com.example.springtask.async.service.DemoService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.SuccessCallback;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@SpringBootTest
public class AsyncTaskTest {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DemoService demoService;

    @Test
    public void task01() {
        long now = System.currentTimeMillis();
        logger.info("[task01][开始执行]");
        // 同步或异步执行查看时间，同步15s左右
        // demoService.execute01();
        // demoService.execute02();
        // 异步7毫秒执行完毕
        demoService.execute01Async();
        demoService.execute02Async();
        logger.info("[task01][结束执行，消耗时长 {} 毫秒]", System.currentTimeMillis() - now);
    }

    @Test
    public void task03() throws ExecutionException, InterruptedException {
        long now = System.currentTimeMillis();
        logger.info("[task03][开始执行]");

        // <1> 执行任务
        Future<Integer> execute01Result = demoService.execute01AsyncWithFuture();
        Future<Integer> execute02Result = demoService.execute02AsyncWithFuture();
        // <2> 阻塞等待结果，执行时长由最长的任务决定
        execute01Result.get();
        execute02Result.get();
        logger.info("[task03][结束执行，消耗时长 {} 毫秒]", System.currentTimeMillis() - now);
    }

    @Test
    public void task04() throws ExecutionException, InterruptedException {
        long now = System.currentTimeMillis();
        logger.info("[task04][开始执行]");

        // <1> 执行任务
        ListenableFuture<Integer> execute01Result = demoService.execute01AsyncWithListenableFuture();
        logger.info("[task04][execute01Result 的类型是：({})]",execute01Result.getClass().getSimpleName());

        execute01Result.addCallback(new SuccessCallback<Integer>() { // <2.1> 增加成功的回调

            @Override
            public void onSuccess(Integer result) {
                logger.info("[onSuccess][result: {}]", result);
            }

        }, new FailureCallback() { // <2.1> 增加失败的回调

            @Override
            public void onFailure(Throwable ex) {
                logger.info("[onFailure][发生异常]", ex);
            }

        });
        execute01Result.addCallback(new ListenableFutureCallback<Integer>() { // <2.2> 增加成功和失败的统一回调

            @Override
            public void onSuccess(Integer result) {
                logger.info("[onSuccess][result: {}]", result);
            }

            @Override
            public void onFailure(Throwable ex) {
                logger.info("[onFailure][发生异常]", ex);
            }

        });
        // <3> 阻塞等待结果
        execute01Result.get();

        logger.info("[task04][结束执行，消耗时长 {} 毫秒]", System.currentTimeMillis() - now);
    }

}
