package com.example.rest.consumer;

import org.apache.dubbo.config.annotation.DubboReference;

import com.example.rest.RestDemoService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RestConsumer {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"spring/rest-consumer.xml"});
        context.start();
        System.out.println("rest consumer start");
        RestDemoService bean = context.getBean(RestDemoService.class);
        String res = bean.sayHello("hjf");
        System.out.println(res);
    }
}
