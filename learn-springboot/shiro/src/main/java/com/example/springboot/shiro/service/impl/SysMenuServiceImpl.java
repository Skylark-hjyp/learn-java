package com.example.springboot.shiro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.shiro.entity.SysMenuEntity;
import com.example.springboot.shiro.mapper.SysMenuMapper;
import com.example.springboot.shiro.service.SysMenuService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenuEntity> implements SysMenuService {

    @Override
    public List<SysMenuEntity> getUserMenuList(long userId) {
        QueryWrapper<SysMenuEntity> query = new QueryWrapper<>();
        query.eq("userId", userId);
        return this.baseMapper.selectList(query);
    }
}
