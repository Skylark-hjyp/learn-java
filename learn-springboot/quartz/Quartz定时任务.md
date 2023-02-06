# Quartz

`Quartz`是一个`支持集群`的`任务调度中间件`，但是并没有可视化等功能。在`Quartz`中`JobDetail`和`Trigger`的概念很重要。`JobDetail`用来绑定我们要执行的任务，以及为`任务命名`和`设定执行次数`。`Trigger`用来`绑定JobDetail`和`设置执行频率`。

使用Quartz执行定时任务主要分为以下步骤：

## 导入依赖

```xml
<dependencies>
    <!-- 实现对 Quartz 的自动化配置 -->
    <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-quartz</artifactId>
    </dependency>
</dependencies>
```

## 定义任务

```java
@Slf4j
public class Job01 extends QuartzJobBean {
    private AtomicInteger counts = new AtomicInteger();
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        // 执行这里的时候会发现总是第1次执行，说明每次执行的Job都是新建的
        log.info("[executeInternal][定时任务1第 ({}) 次执行]", counts.incrementAndGet());
    }
}
```

```java
@Slf4j
public class Job02 extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("[executeInternal][定时器2：我开始的执行了]");
    }
}
```

> 注意：每一次Trigger执行的时候都会重新创建一个Job对象，所以上面Job01永远是第一次执行

## 在配置类中创建JobDetail和Trigger

```java
@Configuration
public class ScheduleConfig {

    public static class DemoJob01Configuration {

        @Bean
        public JobDetail demoJob01() {
            return JobBuilder.newJob(Job01.class)
                    .withIdentity("Job01") // 名字为 demoJob01
                    .storeDurably() // 没有 Trigger 关联的时候任务是否被保留。因为创建 JobDetail 时，还没 Trigger 指向它，所以需要设置为 true ，表示保留。
                    .build();
        }

        @Bean
        public Trigger demoJob01Trigger() {
            // 简单的调度计划的构造器
            SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInSeconds(5) // 频率。
                    .repeatForever(); // 次数。
            // Trigger 构造器
            return TriggerBuilder.newTrigger()
                    .forJob(demoJob01()) // 对应 Job 为 demoJob01
                    .withIdentity("Job01Trigger") // 名字为 demoJob01Trigger
                    .withSchedule(scheduleBuilder) // 对应 Schedule 为 scheduleBuilder
                    .build();
        }

    }

    public static class DemoJob02Configuration {

        @Bean
        public JobDetail demoJob02() {
            return JobBuilder.newJob(Job02.class)
                    .withIdentity("Job02") // 名字为 demoJob02
                    .storeDurably() // 没有 Trigger 关联的时候任务是否被保留。因为创建 JobDetail 时，还没 Trigger 指向它，所以需要设置为 true ，表示保留。
                    .build();
        }

        @Bean
        public Trigger demoJob02Trigger() {
            // 基于 Quartz Cron 表达式的调度计划的构造器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/10 * * * * ? *");
            // Trigger 构造器
            return TriggerBuilder.newTrigger()
                    .forJob(demoJob02()) // 对应 Job 为 demoJob02
                    .withIdentity("Job02Trigger") // 名字为 demoJob02Trigger
                    .withSchedule(scheduleBuilder) // 对应 Schedule 为 scheduleBuilder
                    .build();
        }

    }

}
```