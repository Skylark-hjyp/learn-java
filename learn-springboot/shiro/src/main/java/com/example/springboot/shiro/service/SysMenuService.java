package com.example.springboot.shiro.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.shiro.entity.SysMenuEntity;

import java.util.List;

public interface SysMenuService extends IService<SysMenuEntity> {
    List<SysMenuEntity> getUserMenuList(long userId);
}
