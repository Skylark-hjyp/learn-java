package com.example.mybatis.sharding.mapper;

import com.example.mybatis.sharding.entity.OrderConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrderConfigMapper {
    OrderConfig selectById(@Param("id") Integer id);
}
