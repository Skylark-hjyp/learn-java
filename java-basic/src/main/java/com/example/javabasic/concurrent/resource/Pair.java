package com.example.javabasic.concurrent.resource;

/**
 * 是一个非线程安全的类
 */
public class Pair {
    private int x, y;

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pair() { this(0, 0); }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void incrementX() { x++; }

    public void incrementY() { y++; }

    public String toString() {
        return "x: " + x + ", y: " + y;
    }

    public class PairValuesNotEqualException extends RuntimeException {
        public PairValuesNotEqualException() {
            super("Pair values not equal: " + Pair.this);
        }

        public void chexkState() {
            // 内部类可以访问外部类的变量
            if( x != y) {
                throw new PairValuesNotEqualException();
            }
        }
    }
}
