# Spring Task 定时任务

该定时任务由Spring官方实现，只需要在配置类上使用`@EnableScheduling`注解开启定时任务即可，然后在需要定时执行的方法上使用`@Scheduled(fixedRate = 2000)`注解指定执行时间

`@Scheduled`注解有以下几个属性：

* `cron`：使用表达式来指定任务执行时间。如`"0 0 12 * * ?"` 表示每天中午执行一次
* `fixedDelay` 属性：固定执行间隔，单位：毫秒。注意，以调用**完成时刻**为开始计时时间。
* `fixedRate` 属性：固定执行间隔，单位：毫秒。注意，以调用**开始时刻**为开始计时时间。

## 可以配置的选项

```yaml
spring:
  task:
    # Spring Task 调度任务的配置，对应 TaskSchedulingProperties 配置类
    scheduling:
      thread-name-prefix: pikaqiu-demo- # 线程池的线程名的前缀。默认为 scheduling- ，建议根据自己应用来设置
      pool:
        size: 10 # 线程池大小。默认为 1 ，根据自己应用来设置
      shutdown:
        await-termination: true # 应用关闭时，是否等待定时任务执行完成。默认为 false ，建议设置为 true
        await-termination-period: 60 # 等待任务完成的最大时长，单位为秒。默认为 0 ，根据自己应用来设置
```