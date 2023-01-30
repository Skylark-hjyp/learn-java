package com.example.learnrabbitmq.controller;


import com.example.learnrabbitmq.producer.NotifyProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class MqController {

    @Resource
    private NotifyProducer notifyMsgProducer;

    @GetMapping("/produce")
    public String produce() {
        notifyMsgProducer.send("你好啊");
        return "success";
    }
}

