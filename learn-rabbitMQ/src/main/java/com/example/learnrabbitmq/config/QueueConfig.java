package com.example.learnrabbitmq.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {

    @Bean
    public DirectExchange defaultExchange() {
        return new DirectExchange("DefaultExchange");
    }

    @Bean
    public DirectExchange notifyMsgDirectExchange() {
        return new DirectExchange("NotifyExchange");
    }

    @Bean
    public Queue notifyMsgQueue() {
        return new Queue("NotifyQueue", true);
    }

    @Bean
    public Queue queue1() {
        return new Queue("Queue1", true);
    }


    @Bean
    public Binding notifyMsgQueueBinding() {
        return BindingBuilder
                .bind(notifyMsgQueue())
                .to(notifyMsgDirectExchange())
                .with("NotifyQueue");
    }

    @Bean
    public Binding queue1Binding() {
        return BindingBuilder
                .bind(queue1())
                .to(notifyMsgDirectExchange())
                .with("Queue1");
    }
}

