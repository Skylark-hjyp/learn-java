package com.example.mybatis.mapper;

import com.example.mybatis.entity.Meet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface MeetMapper {
    
    // 新增会议
    int insertMeet();

    // 根据id查询会议
    Meet queryById(@Param("id") Integer id);

    // 根据id查询会议返回map
    Map<String, Object> queryByIdRMap(@Param("id") Integer id);

    // 根据实体查询会议
    Map<String, Object> queryByMeet(Meet meet);

    // 查询所有会议
    List<Meet> queryAll();

}
