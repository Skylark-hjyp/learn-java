package com.example.redis.controller;

import com.example.redis.service.GoodService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/goods")
public class GoodController {

    @Resource
    GoodService goodService;

    @RequestMapping("/secKillByRedis")
    public Boolean secKillByPessimistic(Integer goodId) {
        Boolean res = goodService.secKillByRedis(goodId);
        return res;
    }
}
