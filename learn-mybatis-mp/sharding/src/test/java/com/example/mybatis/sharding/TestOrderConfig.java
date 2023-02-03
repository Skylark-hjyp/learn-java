package com.example.mybatis.sharding;

import com.example.mybatis.sharding.entity.OrderConfig;
import com.example.mybatis.sharding.mapper.OrderConfigMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestOrderConfig {
    @Autowired
    private OrderConfigMapper orderConfigMapper;

    @Test
    public void testSelectById() {
        OrderConfig orderConfig = orderConfigMapper.selectById(1);
        System.out.println(orderConfig);
        // System.out.println("22");
    }
}
