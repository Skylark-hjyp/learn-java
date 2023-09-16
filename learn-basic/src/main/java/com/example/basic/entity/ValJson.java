package com.example.basic.entity;

import java.lang.reflect.Type;

public class ValJson {
    static class Count<T> {
        public T count;

        public void setCount(T count) {
            this.count = count;
        }
    }

    public static void main(String[] args) throws NoSuchFieldException {

        Count<Integer> count = new Count<>();

        count.setCount(2);

        // 由于类型擦除，type 的类型为 java.lang.Object
        Type type = count.getClass().getField("count").getType();

        System.out.println(count);
    }
}


