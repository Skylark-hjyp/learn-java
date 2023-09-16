package com.example.mybatis.controller;

import com.example.mybatis.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/goods")
public class GoodController {

    @Resource
    GoodService goodService;

    @RequestMapping("/secKillByPessimistic")
    public Boolean secKillByPessimistic(Integer goodId) {
        Boolean res = goodService.secKillByPessimistic(goodId);
        return res;
    }

    @RequestMapping("/secKillByPositive")
    public Boolean secKillByPositive(Integer goodId) {
        Boolean res = goodService.secKillByPositive(goodId);
        return res;
    }

    @RequestMapping("/secKillByNoLock")
    public Boolean secKillByNoLock(Integer goodId) {
        Boolean res = goodService.secKillByNoLock(goodId);
        return res;
    }
}
