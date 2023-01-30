package com.example.javabasic.annotation.define;

import java.lang.annotation.*;

/**
 * 该注解作用在类上
 * 使用name域指定表名
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DBTable {
    String name() default "";
}
