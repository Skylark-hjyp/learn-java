package com.example.javabasic.annotation.process;

import com.example.javabasic.annotation.define.UseCase;
import com.example.javabasic.annotation.use.PasswordUtils;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UseCaseTracker {
    public static void trackUseCases(List<Integer> userCases, Class<?> cl) {
        // 1、获取该类下所有的方法
        for (Method m : cl.getDeclaredMethods()) {
            // 2、判断该方法上是否有此注解
            UseCase uc = m.getAnnotation(UseCase.class);
            if (uc != null) {
                System.out.println("Found Use Case:" + uc.id() + " " + uc.description());
                userCases.remove(Integer.valueOf(uc.id()));
            }
        }
        for (int i: userCases) {
            System.out.println(i);
        }
    }

    public static void main(String[] args) {
        List<Integer> useCases = new ArrayList<>();
        Collections.addAll(useCases, 47, 48, 49);
        trackUseCases(useCases, PasswordUtils.class);
    }
}
