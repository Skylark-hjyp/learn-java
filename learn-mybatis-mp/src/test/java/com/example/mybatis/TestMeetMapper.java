package com.example.mybatis;

import com.example.mybatis.entity.Meet;
import com.example.mybatis.mapper.MeetMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
public class TestMeetMapper {

    @Autowired
    private MeetMapper meetMapper;

    @Test
    public void testQueryByMeet() {
        Map<String, Object> result = meetMapper.queryByMeet(new Meet());
        log.info(result.toString());
    }

    @Test
    public void testQueryAll() {
        List<Meet> result = meetMapper.queryAll();
        log.info(result.toString());
        // System.out.println("ss");
    }

    @Test
    public void testQueryByIdRMap() {
        Map<String, Object> result = meetMapper.queryByIdRMap(1);
        log.info(result.toString());
    }
}
