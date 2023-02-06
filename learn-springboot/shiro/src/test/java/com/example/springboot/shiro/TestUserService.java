package com.example.springboot.shiro;

import com.example.springboot.shiro.service.SysUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@SpringBootTest
public class TestUserService {
    @Autowired
    SysUserService sysUserService;

    @Test
    public void testGetUserPermissions() {
        // System.out.println("hello");
        Set<String> res = sysUserService.getUserPermissions(1);
        System.out.println(res);
    }
}
