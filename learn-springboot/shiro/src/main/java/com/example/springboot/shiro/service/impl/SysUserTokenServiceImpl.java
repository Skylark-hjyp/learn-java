package com.example.springboot.shiro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.shiro.entity.SysUserTokenEntity;
import com.example.springboot.shiro.mapper.SysUserTokenMapper;
import com.example.springboot.shiro.service.SysUserTokenService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SysUserTokenServiceImpl extends ServiceImpl<SysUserTokenMapper, SysUserTokenEntity> implements SysUserTokenService {

    // 12小时后过期
    private final static int EXPIRE = 3600 * 12;

    @Override
    public Map<String, Object> createToken(long userId) {
        // <1> 生成一个 token
        // String token = TokenGenerator.generateValue();
        String token = "temp token";
        // <2> 当前时间
        Date now = new Date();
        // <2> 过期时间
        Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

        // <3> 判断是否生成过 token
        SysUserTokenEntity tokenEntity = this.getById(userId);
        if (tokenEntity == null) { // 新增 SysUserTokenEntity
            tokenEntity = new SysUserTokenEntity();
            tokenEntity.setUserId(userId);
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);

            // 保存 token
            this.save(tokenEntity);
        } else { // 更新 SysUserTokenEntity
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);

            // 更新 token
            this.updateById(tokenEntity);
        }

        // <4> 返回 token 和过期时间
        Map<String, Object> res = new HashMap<>();
        res.put("token", token);
        res.put("expire", EXPIRE);
        return res;
    }

    @Override
    public SysUserTokenEntity queryByToken(String token) {
        return this.baseMapper.queryByToken(token);
    }
}
