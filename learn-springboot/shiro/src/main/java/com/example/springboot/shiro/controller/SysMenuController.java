package com.example.springboot.shiro.controller;

import com.example.springboot.shiro.entity.SysMenuEntity;
import com.example.springboot.shiro.service.SysMenuService;
import com.example.springboot.shiro.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class SysMenuController {
    // SysMenuController.java

    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 导航菜单
     */
    @GetMapping("/nav")
    public String nav(@RequestParam("userId") long userId) {
        // <1> 获得用户的菜单数组
        // List<SysMenuEntity> menuList = sysMenuService.getUserMenuList(userId);
        // <2> 获得用户的权限集合
        Set<String> permissions = sysUserService.getUserPermissions(userId);
        // <3> 返回
        return permissions.toString();
    }
}
