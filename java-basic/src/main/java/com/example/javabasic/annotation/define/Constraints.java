package com.example.javabasic.annotation.define;

import java.lang.annotation.*;

/**
 * 该注解作用在域上，主要是约束该字段
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Constraints {
    /** 是否是主键约束 */
    boolean primaryKey() default false;
    /** 是否允许为空 */
    boolean allowNull() default true;
    /** 是否唯一 */
    boolean unique() default false;
}
