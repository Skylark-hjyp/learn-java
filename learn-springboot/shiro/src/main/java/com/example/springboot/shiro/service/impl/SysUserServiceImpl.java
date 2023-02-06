package com.example.springboot.shiro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.shiro.entity.SysMenuEntity;
import com.example.springboot.shiro.entity.SysUserEntity;
import com.example.springboot.shiro.mapper.SysMenuMapper;
import com.example.springboot.shiro.mapper.SysUserMapper;
import com.example.springboot.shiro.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserEntity> implements SysUserService {
    @Autowired
    private SysMenuMapper sysMenuDao;

    @Override
    public SysUserEntity queryByUserName(String username) {
        // baseMapper 由 MyBatis-Plus 提供
        return baseMapper.queryByUserName(username);
    }

    @Override
    public Set<String> getUserPermissions(long userId) {
        List<String> permsList;
        // <1.1> 系统管理员，拥有最高权限
        if (userId == 0) {
            // 如果是管理员，则查询所有 SysMenuEntity 数组
            List<SysMenuEntity> menuList = sysMenuDao.selectList(new QueryWrapper<>());
            permsList = new ArrayList<>(menuList.size());
            for (SysMenuEntity menu : menuList) {
                permsList.add(menu.getPerms());
            }
            // <1.2>
        } else {
            // 如果是普通用户，则查询其拥有的 SysMenuEntity 数组
            permsList = baseMapper.queryAllPerms(userId);
        }
        // <2> 用户权限列表
        Set<String> permsSet = new HashSet<>();
        for (String perms : permsList) {
            if (StringUtils.isBlank(perms)) {
                continue;
            }
            // 使用逗号分隔，每一个 perms
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }
}
