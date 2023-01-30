package com.example.javabasic.io;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.regex.Pattern;

public class FileTest {
    public static void main(String[] args) {
        FileTest fileTest = new FileTest();

        // fileTest.commonMethods();

        fileTest.traverseFile("E:\\baidu", 0);
    }

    /**
     * File类常用方法
     */
    public void commonMethods() {
        // 新建File对象
        File file = new File("E:\\hjf");
        // 判断是否存在
        boolean exist = file.exists();
        // 判断是不是文件夹
        boolean isFile = file.isFile();
        // 不存在就创建
        if (!file.exists()) {
            file.mkdirs();
        }
        // 输出文件大小
        System.out.println(file.length());
    }

    /**
     * 深度优先输出所有文件名
     * @param path 文件路径
     * @param step 层级关系
     */
    public void traverseFile(String path, int step) {
        File parentFile = new File(path);
        File[] files = parentFile.listFiles();
        if (files == null) {
            return;
        }
        for (File file: files) {
            // 不论是文件夹还是文件，都要输出文件名称
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < step; i ++) {
                s.append("----");
            }
            s.append(file.getName());
            System.out.println(s);
            //如果是文件夹，则需要传入绝对路径递归处理
            if (file.isDirectory()) {
                traverseFile(file.getAbsolutePath(), step + 1);
            }
        }
    }

    /**
     * 查询文件夹下的所有文件名
     */
    public void listFiles() {
        class DirFilter implements FilenameFilter {
            private Pattern pattern = null;

            public DirFilter(String filter) {
                Pattern.compile(filter);
            }

            @Override
            public boolean accept(File dir, String name) {
                // matcher()方法构建matcher对象，matches()方法判断是否和模板匹配
                return pattern.matcher(name).matches();
            }
        }
        File path = new File("E:\\hjf");
        String[] list;
        String filter = ".*.java";

        // 不使用匿名内部类
        // list = path.list(new DirFilter(filter));

        // 使用匿名内部类
        list = path.list(new DirFilter(filter));

        // 直接实例化接口
        list = path.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return false;
            }
        });
        // 根据字符串排序，排除大小写影响
        Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
        for (String dirName: list) {
            System.out.println(dirName);
        }
    }
}

// class DirFilter implements FilenameFilter {
//     // 匹配串对象
//     private Pattern pattern;
//
//     // 根据模式构建匹配对对象
//     public DirFilter(String regex) {
//         pattern = Pattern.compile(regex);
//     }
//
//     @Override
//     public boolean accept(File dir, String name) {
//         // matcher()方法构建matcher对象，matches()方法判断是否和模板匹配
//         return pattern.matcher(name).matches();
//     }
// }
