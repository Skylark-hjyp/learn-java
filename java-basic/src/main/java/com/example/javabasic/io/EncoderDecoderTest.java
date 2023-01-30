package com.example.javabasic.io;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class EncoderDecoderTest {

    public static void main(String[] args) throws UnsupportedEncodingException {
        //Java使用的默认字符集
        System.out.println(URLEncoder.encode("name=何", "utf-8"));
        System.out.println();
        System.out.println("java 默认字符集：");
        System.out.println(Charset.defaultCharset()+"\n");
        //汉字“我”的字节编码，utf-8占用3个字节，GBK占用两个字节
        String str = "我";
        //这里可以手动设置编码字符集，默认使用utf-8编码
        byte[] bytes = str.getBytes("GBK");
        System.out.println(new String(bytes, "GBK"));
        System.out.println(bytes.length);
    }
}
