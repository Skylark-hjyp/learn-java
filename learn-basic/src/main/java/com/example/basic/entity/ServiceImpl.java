package com.example.basic.entity;

import java.lang.reflect.Array;
import java.time.Period;
import java.util.*;

public class ServiceImpl {
    // public byte[] func(int a) {
    //     System.out.println(a);
    //     return new byte[] {1, 2, 3, 4};
    // }

    class Person<T> {
        private String name = "hjf";
        private Integer[] ages = {1, 2, 3};
        private T templateType;
    }

    public Person person = new Person();

    public ArrayList<String> func1(ArrayList<Character> a) {
        System.out.println(a);
        return new ArrayList<>(Arrays.asList("123", "hello"));
    }


}
