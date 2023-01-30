package com.example.javabasic.annotation.define;
import java.lang.annotation.*;

/**
 * 该注解作用在域上，指明该字段为String类型
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SQLString {
    /**
     * 指定string所占的字符长度
     * value方法在使用注解时可以不指定key值
     * @return
     */
    int value() default 0;

    /**
     * 指定该列的名称
     * @return
     */
    String name() default "";

    /**
     * 嵌套注解，嵌套的注解也可以有默认值
     * @return
     */
    Constraints constrains() default @Constraints(primaryKey = false);
}
