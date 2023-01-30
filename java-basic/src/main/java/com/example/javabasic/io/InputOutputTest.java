package com.example.javabasic.io;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class InputOutputTest {
    public static void main(String[] args) throws IOException {
        InputOutputTest inputOutputTest = new InputOutputTest();

        // 从文件中读取内容
        // String result = inputOutputTest.readFileWithBuffer("E:\\test.txt");
        // System.out.println(result);

        // 从String内存中读取
        // inputOutputTest.memoryInput();

        //向文件中写入字符串
        // inputOutputTest.basicFileOutput();

        // 向文件中写入对象
        // inputOutputTest.saveAndReadData();

        // 从标准输入中读取
        // inputOutputTest.readFromSystem();

        // 重定向控制台
        // inputOutputTest.redirect();

        inputOutputTest.writeAndReadChar();
        // System.out.println('a');
    }

    /**
     * 向文件中写入并读取字符
     * @throws IOException
     */
    public void writeAndReadChar() throws IOException {
        // 1、向文件中写入字符
        try (BufferedWriter out = new BufferedWriter(new FileWriter("test.txt"))) {
            out.write("我爱你中国");
        }

        // 2、读取文件中字符
        try (BufferedReader in = new BufferedReader(new FileReader("test.txt"))) {
            int len = 0;
            char[] chars = new char[8];
            // 读入到char数组，len表示本次读取的字符数，可能为零，但为零时并不代表结束。只有为-1才代表结束
            while ((len = in.read(chars)) != -1) {
                for (int i = 0; i < len; i ++) {
                    System.out.println(chars[i]);
                }
            }
        }
    }


    /**
     * 使用缓冲类包装FileReader类
     * @param fileName
     * @return
     * @throws IOException
     */
    public String readFileWithBuffer(String fileName) throws IOException {
        // 使用bufferedReader装饰FileReader对象，提供缓冲功能
        // 使用UTF-8格式读取字符, 因为txt默认也是utf-8编码格式
        BufferedReader in = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8));
        String s;
        StringBuilder sb = new StringBuilder();
        // 读取每一行，直到读到空行为止
        while ((s = in.readLine()) != null) {
            sb.append(s).append('\n');
        }
        in.close();
        return sb.toString();
    }

    /**
     * 读取内存内的String
     * @throws IOException
     */
    public void memoryInput() throws IOException {
        StringReader stringReader = new StringReader("我爱你中国");
        int c;
        while ((c = stringReader.read()) != -1) {
            System.out.println((char) c);
        }
    }

    /**
     * 向文件内写入内容
     * @throws IOException
     */
    public void basicFileOutput() throws IOException {
        BufferedReader in = new BufferedReader(new StringReader("我爱你 中国"));
        //PrintWriter和BufferedWriter都是装饰器，实现了Writer中的方法，也自定义了自己的方法
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("test.txt")));
        int lineCount = 1;
        String s;
        while ((s = in.readLine()) != null) {
            out.println(lineCount++ + ": " + s);
        }
        out.close();
    }

    /**
     * 向文件中保存布尔型和数值
     * @throws IOException
     */
    public void saveAndReadData() throws IOException {
        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("test.txt")));
        out.writeBoolean(false);
        out.writeDouble(3.14);
        out.close();

        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream("test.txt")));
        System.out.println(in.readBoolean());
        System.out.println(in.readDouble());
    }

    /**
     * 从控制台读入
     * @throws IOException
     */
    public void readFromSystem() throws IOException {
        // 使用InputStreamReader类将控制台的字节流转换为字符流
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String s;
        // 读取每一行
        while ((s = in.readLine()) != null && s.length() != 0) {
            // 输出到控制台
            System.out.println(s);
        }
    }

    /**
     * 重定向输出控制台到某个文件
     * @throws IOException
     */
    public void redirect() throws IOException {
        // 保存原来的控制台对象
        PrintStream console = System.out;
        // 新建我们自己的文件重定向控制台输出对象
        PrintStream out = new PrintStream(new BufferedOutputStream(new FileOutputStream("test.java")));
        // 重新设置输出对象
        System.setOut(out);
        // 读取控制台的每一行
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String s;
        // 读取每一行
        while ((s = in.readLine()) != null && s.length() != 0) {
            // 输出到控制台
            System.out.println(s);
        }
        out.close();
        // 恢复原来的控制台输出对象
        System.setOut(console);
    }
}
