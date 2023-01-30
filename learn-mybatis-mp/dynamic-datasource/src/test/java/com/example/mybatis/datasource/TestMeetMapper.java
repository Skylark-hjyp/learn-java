package com.example.mybatis.datasource;

import com.example.mybatis.datasource.entity.Meet;
import com.example.mybatis.datasource.mapper.MeetMapper;
import com.example.mybatis.datasource.mapper.MeetMapper2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestMeetMapper {
    @Autowired
    private MeetMapper meetMapper;

    @Autowired
    private MeetMapper2 meetMapper2;

    @Test
    public void testQueryById() {
        // System.out.println("2");
        Meet meet = meetMapper.queryById(2);
        System.out.println(meet);

        meet = meetMapper2.queryById(2);
        System.out.println(meet);
    }
}
