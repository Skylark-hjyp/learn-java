package com.example.rest.compatibility;


public class RestDemoServiceImpl implements RestDemoService {
    @Override
    public String sayHello(String name) {
        return "hello";
    }

    @Override
    public String sayHi() {
        return "hi";
    }

    @Override
    public Apple sayFruit() {
        return new Apple();
    }

    @Override
    public Apple sayApple() {
        return new Apple();
    }
}
