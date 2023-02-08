package com.example.learnrabbitmq.consumer;

import com.example.learnrabbitmq.message.Demo01Message;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Consumer {

    /**
     * 指定队列消费者线程数
     * @param vo
     */
    @RabbitListener(queues = "Queue1", concurrency = "2")
    public void Send(String vo) {
        System.out.println(Thread.currentThread().getId());
        System.out.println("消费者2收到消息:" + vo);
    }


    /**
     * Direct Exchange 交换机下的消费者
     * @param message
     */
    @RabbitListener(queues = "DirectExchangeQueue")
    public void onMessage1(Demo01Message message) {
        log.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }

//    如果想要获取消息更详细的信息，比如RoutingKey、创建时间等，则可以传入org.springframework.amqp.core.Message作为参数
//    @RabbitHandler(isDefault = true)
//    public void onMessage(org.springframework.amqp.core.Message message) {
//        logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
//    }

    /**
     * 批量消息的消费者，也是一条条消费的
     * @param message
     */
    @RabbitListener(queues = "BatchProducerMessageQueue")
    public void onMessage2(String message) {
        log.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }

    /**
     * 重试消息的消费者，会抛出异常，模拟接受失败
     * @param message
     */
    @RabbitListener(queues = "RepeatSendQueue")
    public void repeatSendMessage(String message) {
        log.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
        // <X> 注意，此处抛出一个 RuntimeException 异常，模拟消费失败, 这样就可以重新发送消息或者丢入死信队列
        throw new RuntimeException("我就是故意抛出一个异常");
    }

    /**
     * 死信队列的消费者
     * @param message
     */
    @RabbitListener(queues = "RepeatSendDeadQueue")
    public void deadMessage(String message) {
        log.info("[onMessage][【死信队列】线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }

}
