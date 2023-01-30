package com.example.validation.controller;

import com.example.validation.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/users")
// 必须有该注解@Validated，才能使用@Min等具体的注解
@Validated
@Slf4j
public class UserController {

    @GetMapping("/get")
    public void get(@RequestParam("id") @Min(value = 1L, message = "编号必须大于零") Integer id) {
        log.info("[get][id2: {}]", id);
    }

    @PostMapping("/add")
    public void add(@Valid User user) {
        log.info("[add][User: {}]", user);
    }
}
