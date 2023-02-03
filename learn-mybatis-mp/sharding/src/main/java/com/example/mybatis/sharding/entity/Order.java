package com.example.mybatis.sharding.entity;


import lombok.Data;

@Data
public class Order {
    /** 订单编号 */
    private Long id;

    /** 用户编号 */
    private Integer userId;
}
