# Rabbit MQ

Rabbit MQ有几个非常重要的概念，`交换机（Exchange）`、`队列（Queue）`、`绑定关系（Binding，绑定交换机和队列）`、`生产者（Producer）`、`消费者（Consumer）`。

使用时，我们需要

* 1、在配置类中`声明队列，交换机，以及队列与交换机之间的绑定关系`
* 2、编写生产者代码，使用`RabbitTemplate.convertAndSend()`方法向`指定交换机发送消息`
* 3、编写消费者代码，使用`@RabbitListener`注解`监听队列消息`

## 交换机的类型

* Direct Exchange：直连交换机，根据Routing Key(路由键)进行投递到不同队列。

* Fanout Exchange：扇形交换机，采用广播模式，根据绑定的交换机，路由到与之对应的所有队列。

* Topic Exchange：主题交换机，对路由键进行模式匹配后进行投递，符号#表示一个或多个词，*表示一个词。

* Header Exchange：头交换机，不处理路由键。而是根据发送的消息内容中的headers属性进行匹配。

## 基本使用

### 引入依赖

```xml
<!--    RabbitMQ 依赖    -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

### 声明队列、交换机和绑定关系

```java
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
```

### 创建生产者

```java
@Component
@Slf4j
public class Producer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

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
}
```

### 创建消费者

```java
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
}
```

## 批量发送消息

RabbitMQ本身并没有批量发送消息的API，但是`Spring-AMQP`提供了批量发送消息的实现，使用`BatchingRabbitTemplate`的`convertAndSend`方法发送消息即可。具体原理为它提供了一个 [MessageBatch](https://github.com/spring-projects/spring-amqp/blob/master/spring-rabbit/src/main/java/org/springframework/amqp/rabbit/batch/MessageBatch.java) 消息收集器，将发送给相同 `Exchange + RoutingKey` 的消息们，“偷偷”收集在一起，当满足条件时候，一次性批量发送提交给 `RabbitMQ Broker `。

批量发送可能触发的条件：

* 超过收集消息的最大数量
* 所收集的数据超过最大缓冲容量
* 距离上一次发送时间超过最长等待时间

### 配置队列、交换机、绑定关系、以及BatchTemplate

```java
/**
 * 批量发送消息配置类
 */
@Configuration
public class BatchProducerConfig {
    /**
     * Direct Exchange 示例的配置类
     */
    public static class DirectExchangeDemoConfiguration {

        // 创建 Queue
        @Bean
        public Queue demo05Queue() {
            return new Queue("BatchProducerMessageQueue", // Queue 名字
                    true, // durable: 是否持久化
                    false, // exclusive: 是否排它
                    false); // autoDelete: 是否自动删除
        }

        // 创建 Direct Exchange
        @Bean
        public DirectExchange demo05Exchange() {
            return new DirectExchange("BatchProducerMessageExchange",
                    true,  // durable: 是否持久化
                    false);  // exclusive: 是否排它
        }

        // 创建 Binding
        // Exchange：Demo05Message.EXCHANGE
        // Routing key：Demo05Message.ROUTING_KEY
        // Queue：Demo05Message.QUEUE
        @Bean
        public Binding demo05Binding() {
            return BindingBuilder.bind(demo05Queue()).to(demo05Exchange()).with("BatchProducerMessageRoutingKey");
        }

    }

    @Bean
    public BatchingRabbitTemplate batchRabbitTemplate(ConnectionFactory connectionFactory) {
        // 创建 BatchingStrategy 对象，代表批量策略
        int batchSize = 16384; // 超过收集的消息数量的最大条数。
        int bufferLimit = 33554432; // 每次批量发送消息的最大内存
        int timeout = 30000; // 超过收集的时间的最大等待时长，单位：毫秒
        BatchingStrategy batchingStrategy = new SimpleBatchingStrategy(batchSize, bufferLimit, timeout);

        // 创建 TaskScheduler 对象，用于实现超时发送的定时器
        TaskScheduler taskScheduler = new ConcurrentTaskScheduler();

        // 创建 BatchingRabbitTemplate 对象
        BatchingRabbitTemplate batchTemplate = new BatchingRabbitTemplate(batchingStrategy, taskScheduler);
        batchTemplate.setConnectionFactory(connectionFactory);
        return batchTemplate;
    }
}
```

### 生产者

```java
// Producer 代码
/**
* 批量发送消息
* @param message
*/
public void batchSendMessage(String message) {
    // 同步发送消息
    batchingRabbitTemplate.convertAndSend("BatchProducerMessageExchange", "BatchProducerMessageRoutingKey", message);
}
```

### 消费者

```java
/**
* 批量消息的消费者，也是一条条消费的
* @param message
*/
@RabbitListener(queues = "BatchProducerMessageQueue")
public void onMessage2(String message) {
    log.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
}
```

## 批量消费消息

## 消费重试

当消息消费失败后可以尝试重新发送消费消息，如果重试指定次数后仍然失败，那么消息会被发送到死信队列，进行消费。

### 配置文件

```yaml
spring:
  # RabbitMQ 配置项，对应 RabbitProperties 配置类
  rabbitmq:
    host: 127.0.0.1 # RabbitMQ 服务的地址
    port: 5672 # RabbitMQ 服务的端口
    username: guest # RabbitMQ 服务的账号
    password: guest # RabbitMQ 服务的密码
    listener:
      simple:
        # 对应 RabbitProperties.ListenerRetry 类
        retry:
          enabled: true # 开启消费重试机制
          max-attempts: 3 # 最大重试次数。默认为 3 。
          initial-interval: 1000 # 重试间隔，单位为毫秒。默认为 1000 。

```

### 配置队列、死信队列、交换机以及绑定关系

```java
/**
 * 重试发送消息配置类
 */
@Configuration
public class RepeatSendConfig {
    // 创建 Queue
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
```

## 延迟队列

## 集群消费

## 并发消费

在`@RabbitListener注解`中使用`concurrency`属性，例如`@RabbitListener(queues = "Queue1", concurrency = "2")`

## 顺序消息

## 事务消息

## 消费者的消息确认

## 发送者的发送确认

## RPC远程调用

## MessageConverter

## 异常处理