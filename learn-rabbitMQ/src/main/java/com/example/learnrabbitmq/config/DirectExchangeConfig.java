package com.example.learnrabbitmq.config;

import com.example.learnrabbitmq.message.Demo01Message;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectExchangeConfig {

    @Bean
    public DirectExchange notifyDirectExchange() {
        return new DirectExchange("NotifyExchange");
    }


    @Bean
    public Queue queue1() {
        return new Queue("Queue1", true);
    }

    @Bean
    public Binding queue1Binding() {
        return BindingBuilder
                .bind(queue1())
                .to(notifyDirectExchange())
                .with("Queue1");
    }

    /**
     * Direct Exchange 示例的配置类
     */
    public static class DirectExchangeDemoConfiguration {

        // 创建 Queue
        @Bean
        public Queue demo01Queue() {
            return new Queue("DirectExchangeQueue", // Queue 名字
                    true, // durable: 是否持久化
                    false, // exclusive: 是否排它
                    false); // autoDelete: 是否自动删除
        }

        // 创建 Direct Exchange
        @Bean
        public DirectExchange demo01Exchange() {
            return new DirectExchange("DirectExchange",
                    true,  // durable: 是否持久化
                    false);  // exclusive: 是否排它
        }

        // 创建 Binding
        // Exchange：Demo01Message.EXCHANGE
        // Routing key：Demo01Message.ROUTING_KEY
        // Queue：Demo01Message.QUEUE
        @Bean
        public Binding demo01Binding() {
            return BindingBuilder.bind(demo01Queue()).to(demo01Exchange()).with("DirectExchangeRoutingKey");
        }
    }
}

