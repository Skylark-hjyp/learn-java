package com.example.validation.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
public class User {
    @NotEmpty(message = "登陆账号不能为空")
    @Length(min = 5, max = 16, message = "账号长度5-16位")
    private String username;

    @NotEmpty(message = "密码不能为空")
    @Length(min = 5, max = 16, message = "密码长度5-16位")
    private String password;
}
