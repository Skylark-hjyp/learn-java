package com.example.learnrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 重试发送消息配置类
 */
@Configuration
public class RepeatSendConfig {
    // 创建 Queue
    // TODO: 这里的队列并没有创建，稍后找一下原因 http://localhost:15672/
    @Bean
    public Queue demo07Queue() {
        return QueueBuilder.durable("RepeatSendQueue") // durable: 是否持久化
                .exclusive() // exclusive: 是否排它
                .autoDelete() // autoDelete: 是否自动删除
                .deadLetterExchange("RepeatSendExchange")  // 消息发送失败后选择的交换机
                .deadLetterRoutingKey("RepeatSendDeadRoutingKey")  // x消息发送失败后的路由
                .build();
    }

    // 创建 Dead Queue 死信队列
    @Bean
    public Queue demo07DeadQueue() {
        return new Queue("RepeatSendDeadQueue", // Queue 名字
                true, // durable: 是否持久化
                false, // exclusive: 是否排它
                false); // autoDelete: 是否自动删除
    }

    // 创建 Direct Exchange
    @Bean
    public DirectExchange demo07Exchange() {
        return new DirectExchange("RepeatSendExchange",
                true,  // durable: 是否持久化
                false);  // exclusive: 是否排它
    }

    // 创建 Binding
    // Exchange：Demo07Message.EXCHANGE
    // Routing key：Demo07Message.ROUTING_KEY
    // Queue：Demo07Message.QUEUE
    @Bean
    public Binding demo07Binding() {
        return BindingBuilder.bind(demo07Queue()).to(demo07Exchange()).with("RepeatSendRoutingKey");
    }

    // 创建 Dead Binding
    // Exchange：Demo07Message.EXCHANGE
    // Routing key：Demo07Message.DEAD_ROUTING_KEY
    // Queue：Demo07Message.DEAD_QUEUE
    @Bean
    public Binding demo07DeadBinding() {
        return BindingBuilder.bind(demo07DeadQueue()).to(demo07Exchange()).with("RepeatSendDeadRoutingKey");
    }
}
