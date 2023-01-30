package com.example.learnrabbitmq.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class NotifyProducer {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void send(String message) {
        log.debug("生产消息【{}】", message);
        this.rabbitTemplate.convertAndSend("NotifyExchange", "NotifyQueue", message);
        this.rabbitTemplate.convertAndSend("NotifyExchange", "Queue1", message);
        this.rabbitTemplate.convertAndSend("NotifyExchange", "Queue1", "第二条消息");
    }

}
