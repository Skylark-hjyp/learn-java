package com.example.springboot.shiro.config;

import com.example.springboot.shiro.entity.SysUserEntity;
import com.example.springboot.shiro.entity.SysUserTokenEntity;
import com.example.springboot.shiro.service.SysUserService;
import com.example.springboot.shiro.service.SysUserTokenService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class OAuth2Realm extends AuthorizingRealm {
    @Autowired
    private SysUserTokenService sysUserTokenService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 重写支持的token类型
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 拿到token信息
        String accessToken = (String) token.getPrincipal();
        // <1> 根据 accessToken ，查询用户信息
        SysUserTokenEntity tokenEntity = sysUserTokenService.queryByToken(accessToken);
        // token 失效
        if (tokenEntity == null || tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()) {
            throw new IncorrectCredentialsException("token失效，请重新登录");
        }

        // <2> 查询用户信息
        SysUserEntity user = sysUserService.getById(tokenEntity.getUserId());
        // 账号锁定
        if (user.getStatus() == 0) {
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }

        // <3> 创建 SimpleAuthenticationInfo 对象
        return new SimpleAuthenticationInfo(user, accessToken, getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // <1> 获得 SysUserEntity 对象
        SysUserEntity user = (SysUserEntity) principals.getPrimaryPrincipal();
        Long userId = user.getUserId();

        // <2> 用户权限列表
        Set<String> permsSet = sysUserService.getUserPermissions(userId);

        // <3> 创建 SimpleAuthorizationInfo 对象
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }
}
