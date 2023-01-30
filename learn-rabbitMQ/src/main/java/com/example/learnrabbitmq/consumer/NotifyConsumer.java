package com.example.learnrabbitmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotifyConsumer {

    @RabbitListener(queues = "NotifyQueue")
    public void msgSend(String vo) {
        System.out.println(Thread.currentThread().getId());
        System.out.println("消费者1收到消息:" + vo);
    }

    @RabbitListener(queues = "Queue1", concurrency = "2")
    public void Send(String vo) {
        System.out.println(Thread.currentThread().getId());
        System.out.println("消费者2收到消息:" + vo);
    }
}

