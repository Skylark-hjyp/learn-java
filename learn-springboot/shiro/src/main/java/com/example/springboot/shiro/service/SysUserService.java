package com.example.springboot.shiro.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.shiro.entity.SysUserEntity;

import java.util.Set;

public interface SysUserService extends IService<SysUserEntity> {
    SysUserEntity queryByUserName(String username);

    Set<String> getUserPermissions(long userId);
}
