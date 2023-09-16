package com.example.rest.consumer;

import org.apache.dubbo.config.annotation.DubboReference;

import com.example.rest.RestDemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Task implements CommandLineRunner {

    @DubboReference(interfaceClass = RestDemoService.class, protocol = "rest")
    private RestDemoService service;

    @Override
    public void run(String... args) throws Exception {
        String res = service.sayHello("hjf");
        log.info(res);
    }
}
