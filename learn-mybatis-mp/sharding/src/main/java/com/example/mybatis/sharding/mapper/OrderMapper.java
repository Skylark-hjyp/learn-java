package com.example.mybatis.sharding.mapper;

import com.example.mybatis.sharding.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OrderMapper {
    Order selectById(@Param("id") Integer id);

    List<Order> selectListByUserId(@Param("userId") Integer userId);

    void insert(Order order);
}
