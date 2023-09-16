package com.example.basic;

public class TestClass {
    public static void main(String[] args) {
        int[] arr = {0, 1, 2, 3};
        int n = 3;
        int res = 0;
        for (int i = 1; i <= n; i ++) {
            int num = 0;
            for (int j = 1; j <= i; j ++) {
                num += (n - i + 1) * j;
            }
            res = res + num * arr[i];
        }
        System.out.println(res);
    }
}
