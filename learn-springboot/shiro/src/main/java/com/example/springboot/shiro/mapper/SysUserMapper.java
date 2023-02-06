package com.example.springboot.shiro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.shiro.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUserEntity> {
    @Select("select * from sys_user where username = #{username}")
    SysUserEntity queryByUserName(String username);

    @Select("select m.perms from sys_user_role ur \n" +
            "\t\tLEFT JOIN sys_role_menu rm on ur.role_id = rm.role_id \n" +
            "\t\tLEFT JOIN sys_menu m on rm.menu_id = m.menu_id \n" +
            "\twhere ur.user_id = #{userId}")
    List<String> queryAllPerms(long userId);
}
