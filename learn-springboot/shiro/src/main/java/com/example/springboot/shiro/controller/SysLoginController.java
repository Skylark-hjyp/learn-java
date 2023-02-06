package com.example.springboot.shiro.controller;

import com.example.springboot.shiro.entity.SysUserEntity;
import com.example.springboot.shiro.service.SysUserService;
import com.example.springboot.shiro.service.SysUserTokenService;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SysLoginController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserTokenService sysUserTokenService;
    // @Autowired
    // private SysCaptchaService sysCaptchaService;

    @PostMapping("/sys/login")
    public String login(@RequestParam("name") String name, @RequestParam("password") String password) {
        // <1> 验证图片验证码的正确性
        // boolean captcha = sysCaptchaService.validate(form.getUuid(), form.getCaptcha());

        boolean captcha = true;
        if (!captcha) {
            return "验证码不正确";
        }

        // <2> 获得之地当用户名的 SysUserEntity
        SysUserEntity user = sysUserService.queryByUserName(name);
        if (user == null || !user.getPassword().equals(new Sha256Hash(password, user.getSalt()).toHex())) { // 账号不存在、密码错误
            return "账号或密码不正确";
        }
        if (user.getStatus() == 0) { // 账号锁定
            return "账号已被锁定,请联系管理员";
        }

        // <3> 生成 Token ，并返回结果
        return sysUserTokenService.createToken(user.getUserId()).toString();
    }
}
