package com.example.springtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringTask {
    public static void main(String[] args) {
        SpringApplication.run(SpringTask.class, args);
        System.out.println("Hello world!");
    }
}