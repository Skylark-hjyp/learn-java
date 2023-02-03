package com.example.mybatis.sharding;

import com.example.mybatis.sharding.entity.Order;
import com.example.mybatis.sharding.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestOrderMapper {
    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void testSelectById() {
        // 可以看到因为该字段不是分片规则，所以查询了8次，即查询所有数据库表
        Order order = orderMapper.selectById(1);
        System.out.println(order);
    }

    @Test
    public void testSelectListByUserId() {
        // 因为该字段
        List<Order> orders = orderMapper.selectListByUserId(1);
        System.out.println(orders.size());
    }

    @Test
    public void testInsert() {
        Order order = new Order();
        order.setUserId(1);
        orderMapper.insert(order);
    }
}
