package com.example.javabasic.io;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class NIOTest {
    public static void main(String[] args) throws IOException {
        // String s = "/home//foo/";
        // String[] paths = s.split("/");
        // System.out.println(paths);

        NIOTest nio = new NIOTest();

        // nio.saveAndReadFile();

        nio.byteBufferView();
    }

    /**
     * 使用NIO写入并重新读取文件, NIO和IO在读取写入时本质上是一样的，都使用的同步非阻塞读写（操作系统的新特性）。
     * 只不过NIO引入了通道和缓冲区的概念，必须有缓冲区，且只能从缓冲区读取数据。方便用一个线程监视多个通道，后面通道可以注册到Selector上。
     * 原来的IO也可以有缓冲区(用BufferedInputStream装饰)，但是原来的IO可以直接从通道读取字节，NIO只能先从通道读到缓冲区，然后从缓冲区读取字节
     * @throws IOException
     */
    public void saveAndReadFile() throws IOException {
        // 1、写入文件
        try (FileChannel fileChannel = new FileOutputStream("test.txt").getChannel()) {
            // 使用wrap方法直接获取String的字节数组
            ByteBuffer byteBuffer = ByteBuffer.wrap("some 中国".getBytes());
            fileChannel.write(byteBuffer);
        }
        // 2、继续写一些文字
        try (FileChannel fileChannel = new RandomAccessFile("test.txt", "rw").getChannel()){
            ByteBuffer byteBuffer = ByteBuffer.wrap(" more bytes".getBytes());
            // 移动写指针到最后，这样就会在末尾添加字符
            fileChannel.position(fileChannel.size());
            // 写入channel
            fileChannel.write(byteBuffer);
        }
        // 3、读取文件
        // 创建NIO的通道, FileChannel是个接口。先创建InputStream，然后再获取通道channel对象
        Charset charset = StandardCharsets.UTF_8;
        try (FileChannel fileChannel = new FileInputStream("test.txt").getChannel()) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(8);
            // 为了解决中文问题，将ByteBuffer转换为CharBuffer
            CharBuffer charBuffer = CharBuffer.allocate(8);
            int len;
            // 只要channel不为空就一直读
            while ((len = fileChannel.read(byteBuffer)) != -1) {
                // 调整缓冲区指针，以便输出流读取
                byteBuffer.flip();
                // 重要：将ByteBuffer转换为CharBuffer, 解码字节流。通过charset实现了CharBuffer和ByteBuffer的互换
                charset.newDecoder().decode(byteBuffer, charBuffer, true);
                charBuffer.flip();
                // 注意直接使用byteBuffer.asCharBuffer()方法是不可以的，因为编码时我们用的是UTF-8，但是该方法解码使用的是utf-16
                // 解决方法有两种，一种是编码时也使用utf-16，这样直接使用asCharBuffer()方法即可；
                // 另一种方法是解码时使用utf-8，将字节流解码为字符流
                // charBuffer = byteBuffer.asCharBuffer();
                // charBuffer.flip();
                System.out.println(charBuffer);
                // 判断是否还有缓冲区是否还有字符
                while (charBuffer.hasRemaining()) {
                    System.out.println((char) charBuffer.get());
                }
                // 清空缓冲区，以便下一次读取存放
                byteBuffer.clear();
                charBuffer.clear();
            }
        }
    }

    /**
     * 使用字节的视图将字节翻译成字符型或者数值型
     * TODO: 那么应该如何将IntBuffer
     */
    public void byteBufferView() throws IOException{

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 创建字节数组的整型视图,可以将ByteBuffer转为IntBuffer
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        // 通过视图向字节数组中输入数据，通过put方法把int[] 转换为 IntBuffer
        intBuffer.put(new int[] {11, 12, 13, 14});
        intBuffer.put(2, 29);
        // 回到初始状态，准备读取内容
        intBuffer.flip();
        // 依次读取每个数字
        while(intBuffer.hasRemaining()) {
            int i = intBuffer.get();
            System.out.println(i);
        }
        try (FileChannel fileChannel = new FileOutputStream("test.txt").getChannel()) {
            fileChannel.position(fileChannel.size());
            byteBuffer.flip();
            fileChannel.write(byteBuffer);
        }

    }
}
