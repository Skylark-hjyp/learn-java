# Redis秒杀方案

`Redis`性能很好，被大量使用于秒杀场景下，实现秒杀有以下几种方案：
## 方案一：使用`商品ID`作为分布式锁，加锁后扣减库存

该方案的`实现流程`为:

* 用户发起秒杀请求到`Redis`，`Redis`先使用`商品ID`作为`key`尝试加锁，保证只有一个用户进入之后流程，保证`原子性`；
* 如果加锁`成功`，则查询库存。如果库存充足，则扣减库存，代表秒杀成功；若库存不足，直接返回秒杀失败；

实现代码如下：

```java
/**
 * Redis秒杀方法一：先加分布式锁，然后查询缓存，根据库存量数量进行后续操作：如果库存量大于零，则扣减库存，返回true；否则返回false；
 * @param goodId 商品ID
 * @return 成功返回true，失败返回false
 */
@Override
public Boolean secKillByRedisFun1(Integer goodId) {
    // 根据商品ID构造key
    String goodKey = "good-stock-" + goodId;
    String userId = Thread.currentThread().getName() + "-" + System.currentTimeMillis();
    // 使用商品作为锁，锁的粒度较大
    String lockId = "sec-kill-lock-" + goodId;
    return this.subStock(lockId, userId, goodKey);
}

/**
 * 使用分布式锁秒杀，加锁后再查询redis库存，最后扣减库存
 * @param lockId 锁ID
 * @param userId 用户ID
 * @param goodKey　商品ID
 * @return 秒杀成功返回 true，否则返回 false
 */
private boolean subStock(String lockId, String userId, String goodKey) {
    // 尝试先加锁，如果加锁成功再进行查询库存量，和扣减库存操作，此时只能有一个线程进入代码块
    if (redisLock.lock(lockId, userId, 4000)) {
        try {
            // 查询库存
            Integer stock = (Integer) redisTemplate.opsForValue().get(goodKey);
            if (stock == null) {
                System.out.println("商品不在缓存中");
            }
            // 如果剩余库存量大于零，则扣减库存
            if (stock > 0) {
                redisTemplate.opsForValue().decrement(goodKey);
                return true;
            } else {
                return false;
            }
        } finally {
            // 释放锁
            redisLock.unlock(lockId, userId);
        }
    }
    return false;
}
```
该方案存在一些`缺点`：

* 用户进来后都要抢锁，即便是库存量`已经为零`，仍然需要抢锁，这无疑带来了很多无用争抢；
* 锁的是商品`ID`，锁粒度太大，并发性能可以进一步优化；

解决方案：

* 抢锁前先`查询库存`，如果库存已经为零，则直接返回`false`，不必参与抢锁过程；
* 使用`商品ID+库存量`作为锁，降低锁粒度，进一步提升并发性能;

## 方案二：使用`商品ID+库存量`作为分布式锁，加锁后扣减库存

该方案的`实现流程`为:

* 用户发起秒杀请求到`Redis`，`Redis`先查询库存量，然后根据`商品ID+库存量`作为`key`尝试加锁，保证只有一个用户进入之后流程，保证`原子性`；
* 如果加锁`成功`，则查询库存。如果库存充足，则扣减库存，代表秒杀成功；若库存不足，直接返回秒杀失败；

> 注意：第一步查询库存量后，可以添加判断`库存是否为零`的操作，这样就能过滤掉库存为零后的大量请求。

实现代码如下：

```java
@Override
public Boolean secKillByRedisFun2(Integer goodId) {
    // 根据商品ID构造key
    String goodKey = "good-stock-" + goodId;
    // 查询库存，使用库存量作为锁，细化锁粒度，提高并发量
    Integer curStock = (Integer) redisTemplate.opsForValue().get(goodKey);
    if (curStock <= 0) {
        return false;
    }
    String userId = Thread.currentThread().getName() + "-" + System.currentTimeMillis();
    String lockId = "sec-kill-lock-" + goodId + "-" + curStock;
    return this.subStock(lockId, userId, goodKey);
}

/**
 * 使用分布式锁秒杀，加锁后再查询redis库存，最后扣减库存
 * @param lockId 锁ID
 * @param userId 用户ID
 * @param goodKey　商品ID
 * @return 秒杀成功返回 true，否则返回 false
 */
private boolean subStock(String lockId, String userId, String goodKey) {
    // 尝试先加锁，如果加锁成功再进行查询库存量，和扣减库存操作，此时只能有一个线程进入代码块
    if (redisLock.lock(lockId, userId, 4000)) {
        try {
            // 查询库存
            Integer stock = (Integer) redisTemplate.opsForValue().get(goodKey);
            if (stock == null) {
                System.out.println("商品不在缓存中");
            }
            // 如果剩余库存量大于零，则扣减库存
            if (stock > 0) {
                redisTemplate.opsForValue().decrement(goodKey);
                return true;
            } else {
                return false;
            }
        } finally {
            // 释放锁
            redisLock.unlock(lockId, userId);
        }
    }
    return false;
}
```

以上两种`先加锁再查询库存量扣减库存`的方案，是为了保证`查询库存`和`扣减库存`操作的原子性，也可以使用`lua`脚本实现这两个操作的原子性，这样就不需要额外维护分布式锁的开销。

## 方案三：使用`INCR`和`DECR`原子操作扣减库存

该方案直接使用`DECR`操作扣减库存，不需要提前查询缓存，代码简洁:

* 如果返回值大于零，说明库存充足，表示秒杀成功；
* 如果返回值小于零，说明库存不足，需要使用`INCR`操作恢复库存，秒杀失败；

实现代码如下：

```java
/**
 * Redis 秒杀方案三：使用原子操作DECR和INCR扣减库存
 * @param goodId 商品ID
 * @return
 */
@Override
public Boolean secKillByRedisFun3(Integer goodId) {
    // 根据商品ID构造key
    String goodKey = "good-stock-" + goodId;
    Long stockCount = redisTemplate.opsForValue().decrement(goodKey);
    if (stockCount >= 0) {
        return true;
    } else {
        // 如果库存不够，则恢复库存
        redisTemplate.opsForValue().increment(goodKey);
        return false;
    }
}
```

**不足**：后期库存为零后，大量请求扣减库存后需要`恢复库存`，这是一个无用操作。

**解决方案**：可以提前查询库存，如果库存为零，直接返回`false`。

