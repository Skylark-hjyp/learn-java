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

    @RequestMapping("/secKillByRedisFun1")
    public Boolean secKillByRedisFun1(Integer goodId) {
        Boolean res = goodService.secKillByRedisFun1(goodId);
        return res;
    }

    @RequestMapping("/secKillByRedisFun2")
    public Boolean secKillByRedisFun2(Integer goodId) {
        Boolean res = goodService.secKillByRedisFun2(goodId);
        return res;
    }

    @RequestMapping("/secKillByRedisFun3")
    public Boolean secKillByRedisFun3(Integer goodId) {
        Boolean res = goodService.secKillByRedisFun3(goodId);
        return res;
    }

    @RequestMapping("/queryGoodCount")
    public Integer queryGoodCount(Integer goodId) {
        String goodKey = "good-stock-" + goodId;
        return goodService.queryGoodCount(goodKey);
    }
}
