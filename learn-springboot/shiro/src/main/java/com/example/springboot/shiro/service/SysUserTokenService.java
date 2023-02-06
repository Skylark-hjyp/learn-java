package com.example.springboot.shiro.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.shiro.entity.SysUserTokenEntity;

import java.util.Map;

public interface SysUserTokenService extends IService<SysUserTokenEntity> {

    Map<String, Object> createToken(long userId);

    SysUserTokenEntity queryByToken(String token);
}
