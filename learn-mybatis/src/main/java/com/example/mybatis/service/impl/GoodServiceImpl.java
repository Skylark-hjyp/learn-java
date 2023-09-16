package com.example.mybatis.service.impl;

import com.example.mybatis.entity.Good;
import com.example.mybatis.mapper.GoodMapper;
import com.example.mybatis.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GoodServiceImpl implements GoodService {

    @Autowired
    private GoodMapper goodMapper;

    @Override
    @Transactional
    public Boolean secKillByPessimistic(Integer goodId) {
        Good good = goodMapper.selectGoodForUpdate(goodId);
        // 如果库存大于零，才会去更新
        if (good.getCount() > 0) {
            good.setCount(good.getCount() - 1);
            Integer res = goodMapper.secKillByPessimistic(good);
            return res == 1;
        }
        return false;
    }

    @Override
    @Transactional
    public Boolean secKillByPositive(Integer goodId) {
        Good good = goodMapper.selectGood(goodId);
        if (good.getCount() > 0) {
            good.setCount(good.getCount() - 1);
            Integer res = goodMapper.secKillByPositive(good);
            return res == 1;
        }
        return false;
    }

    @Override
    @Transactional
    public Boolean secKillByNoLock(Integer goodId) {
        Good good = new Good();
        good.setId(goodId);
        Integer res = goodMapper.secKillByNoLock(good);
        return res == 1;
    }
}
