package com.example.javabasic.annotation.define;

import java.lang.annotation.*;

/**
 * 定义注解
 * 该注解可以作用于方法上，并且存在于运行中，这样可以被我们的注解处理类所处理
 * 该注解包含两个域，int型的id和String型的description，可以为域指定默认值
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UseCase {
    int id();
    String description() default "no description";
}
