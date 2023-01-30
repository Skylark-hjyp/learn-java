package com.example.javabasic.annotation.define;

import java.lang.annotation.*;

/**
 * 该注解作用在域上，指明该字段为Integer类型
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SQLInteger {
    String name() default "";
    Constraints constrains() default @Constraints;
}
