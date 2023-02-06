package com.example.springboot.shiro.config;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 实现自己的Token类，以便在自定义Realm类中调用getPrincipal()方法获取token
 */
public class OAuth2Token implements AuthenticationToken {
    private String token;
    public OAuth2Token(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
