package com.example.javabasic;

import com.example.javabasic.concurrent.resource.Example;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;

class JavaBasicApplicationTests {

    // @Test
    void contextLoads() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);
        Example resource = new Example();

        new Thread(() -> {
            resource.addValue1();
            latch.countDown();
        }).start();

        new Thread(() -> {
            resource.addValue2();
            latch.countDown();
        }).start();

        latch.await();
        System.out.println("执行完毕");
    }
}
