package com.example.redis.service;

public interface GoodService {
    /**
     * Redis秒杀方法一：先加分布式锁，然后查询缓存，根据库存量数量进行后续操作：如果库存量大于零，则扣减库存，返回true；否则返回false；
     * @param goodId 商品ID
     * @return 成功返回true，失败返回false
     */
    Boolean secKillByRedisFun1(Integer goodId);

    /**
     * Redis秒杀方法二：先加分布式锁，然后查询缓存，根据库存量数量进行后续操作：如果库存量大于零，则扣减库存，返回true；否则返回false；
     * 分布式锁细化锁粒度，按库存量作为锁粒度，进一步提升并发量
     * @param goodId 商品ID
     * @return 成功返回true，失败返回false
     */
    Boolean secKillByRedisFun2(Integer goodId);

    /**
     * Redis 秒杀方案三：使用原子操作DECR和INCR扣减库存
     * @param goodId 商品ID
     * @return
     */
    Boolean secKillByRedisFun3(Integer goodId);

    /**
     * 将Redis作为只读缓存，先查询缓存，如果发现没有缓存，则需要去数据库查询，该过程借助分布式锁限制查询数据库线程数量，防止缓存击穿
     * @param goodKey 所要查询的商品key
     * @return 商品对应的库存量
     */
    Integer queryGoodCount(String goodKey);

}
