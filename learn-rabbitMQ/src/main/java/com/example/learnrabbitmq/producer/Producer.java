package com.example.learnrabbitmq.producer;

import com.example.learnrabbitmq.message.Demo01Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Component
@Slf4j
public class Producer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BatchingRabbitTemplate batchingRabbitTemplate;

    /**
     * 生产者发送普通消息
     * @param message
     */
    public void send(String message) {
        log.debug("生产消息【{}】", message);
        // this.rabbitTemplate.convertAndSend("NotifyExchange", "NotifyQueue", message);
        this.rabbitTemplate.convertAndSend("NotifyExchange", "Queue1", message);
        this.rabbitTemplate.convertAndSend("NotifyExchange", "Queue1", "第二条消息");
    }

    /**
     * 指定交换机和路由，同步发送消息
     * @param id
     */
    public void syncSend(Integer id) {
        // 创建 Demo01Message 消息
        Demo01Message message = new Demo01Message();
        message.setId(id);
        // 同步发送消息，指定交换机名、路由地址和消息
        rabbitTemplate.convertAndSend("DirectExchange", "DirectExchangeRoutingKey", message);
    }

    /**
     * 直接指定队列名发送消息
     * @param id
     */
    public void syncSendDefault(Integer id) {
        // 创建 Demo01Message 消息
        Demo01Message message = new Demo01Message();
        message.setId(id);
        // 同步发送消息，当只有两个参数时，会使用默认交换器，隐式地绑定到每个队列，路由键等于队列名称
        rabbitTemplate.convertAndSend("DirectExchangeQueue", message);
    }

    /**
     * 异步发送消息，获取返回值
     * @param id
     * @return
     */
    @Async
    public ListenableFuture<Void> asyncSend(Integer id) {
        try {
            // 发送消息
            this.syncSend(id);
            // 返回成功的 Future
            return AsyncResult.forValue(null);
        } catch (Throwable ex) {
            // 返回异常的 Future
            return AsyncResult.forExecutionException(ex);
        }
    }

    /**
     * 批量发送消息
     * @param message
     */
    public void batchSendMessage(String message) {
        // 同步发送消息
        batchingRabbitTemplate.convertAndSend("BatchProducerMessageExchange", "BatchProducerMessageRoutingKey", message);
    }

    /**
     * 重试发送消息
     * @param message
     */
    public void repeatSendmessage(String message) {
        rabbitTemplate.convertAndSend("RepeatSendExchange", "RepeatSendRoutingKey", message);
    }

}
