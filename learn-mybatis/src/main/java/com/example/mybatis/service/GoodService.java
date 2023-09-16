package com.example.mybatis.service;

public interface GoodService {
    Boolean secKillByPessimistic(Integer goodId);

    Boolean secKillByPositive(Integer goodId);

    Boolean secKillByNoLock(Integer goodId);
}
