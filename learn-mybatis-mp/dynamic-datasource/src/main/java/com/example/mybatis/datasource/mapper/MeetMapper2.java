package com.example.mybatis.datasource.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.example.mybatis.datasource.entity.Meet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@DS("remote")  // 指定使用remote数据源
@Mapper
public interface MeetMapper2 {
    // 根据id查询会议
    Meet queryById(@Param("id") Integer id);
}