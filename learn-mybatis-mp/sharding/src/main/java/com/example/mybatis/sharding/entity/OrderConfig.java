package com.example.mybatis.sharding.entity;

import lombok.Data;

@Data
public class OrderConfig {

    /**
     * id
     */
    private Integer id;

    /**
     * 订单过期时间
     */
    private Integer payTimeout;
}
