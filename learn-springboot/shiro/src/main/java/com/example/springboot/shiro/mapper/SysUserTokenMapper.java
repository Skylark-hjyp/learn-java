package com.example.springboot.shiro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.shiro.entity.SysUserTokenEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysUserTokenMapper extends BaseMapper<SysUserTokenEntity> {

    @Select("select * from sys_user_token where token = #{token}")
    SysUserTokenEntity queryByToken(String token);
}
