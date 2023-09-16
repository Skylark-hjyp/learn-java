package com.example.mybatis.mapper;

import com.example.mybatis.entity.Good;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface GoodMapper {
    Integer secKillByPessimistic(Good good);

    Integer secKillByPositive(Good good);

    Integer secKillByNoLock(Good good);

    Good selectGoodForUpdate(Integer goodId);

    Good selectGood(Integer goodId);
}
