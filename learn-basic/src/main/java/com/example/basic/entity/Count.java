package com.example.basic.entity;

public class Count<T> {
    public Integer number = 0;

    public void add() {
        this.number ++;
    }

    public T count;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public T getCount() {
        return count;
    }

    public void setCount(T count) {
        this.count = count;
    }
}
