package com.example.mybatis.mapper;

import com.example.mybatis.entity.Attach;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper // 使用@Mapper注解和在启动类上使用@MapperScan注解两种方法都可以
public interface AttachMapper {

    // 根据会议ID查询所有的附件列表
    Attach queryById(@Param("meetId") Integer meetId);
    
}
