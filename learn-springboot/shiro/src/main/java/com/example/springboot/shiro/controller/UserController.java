package com.example.springboot.shiro.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @RequestMapping("/user/home")
    @RequiresPermissions("user:home")
    public String home() {
        return "首页";
    }

    @RequestMapping("/user")
    // @RequiresPermissions("user:home")
    public String home2() {
        return "首页";
    }
}
