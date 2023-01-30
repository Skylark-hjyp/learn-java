package com.example.javabasic.annotation.process;

import com.example.javabasic.annotation.define.Constraints;
import com.example.javabasic.annotation.define.DBTable;
import com.example.javabasic.annotation.define.SQLInteger;
import com.example.javabasic.annotation.define.SQLString;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TableCreator {
    public static void main(String[] args) throws ClassNotFoundException {
        String[] classes = new String[] {"com.example.javabasic.annotation.use.Member"};
        // 1、遍历加载每个类
        for (String className: classes) {
            // 加载类
            Class<?> cl = Class.forName(className);
            // 2、从DBTable注解获取表名
            DBTable dbTable = cl.getAnnotation(DBTable.class);
            if (dbTable == null) {
                System.out.println("no DBTable annotations in class " + className);
                continue;
            }
            String tableName = dbTable.name();
            if (tableName.length() < 1) {
                tableName = cl.getName().toUpperCase();
            }
            List<String> columnDefs = new ArrayList<>();
            // 3、遍历每一个field
            for (Field field : cl.getDeclaredFields()) {
                String columnName;
                // 4、获取该field上所有注解
                Annotation[] annotations = field.getAnnotations();
                if (annotations.length < 1) {
                    continue;
                }
                // 4.1 如果该注解是整型注解
                if (annotations[0] instanceof SQLInteger) {
                    SQLInteger sInt = (SQLInteger) annotations[0];
                    if (sInt.name().length() < 1) {
                        columnName = field.getName().toUpperCase();
                    } else {
                        columnName = sInt.name();
                    }
                    columnDefs.add(columnName + " Int " + getConstrains(sInt.constrains()));
                }
                // 4.2 如果该注解是字符串注解
                if (annotations[0] instanceof SQLString) {
                    SQLString sqlString = (SQLString) annotations[0];
                    if (sqlString.name().length() < 1) {
                        columnName = field.getName().toUpperCase();
                    } else {
                        columnName = sqlString.name();
                    }
                    columnDefs.add(columnName + " Varchar(" + sqlString.value() + ")" + getConstrains(sqlString.constrains()));
                }
            }
            StringBuilder createCommand = new StringBuilder("CREATE TABLE " + tableName + "(");
            for (String columnDef : columnDefs) {
                createCommand.append("\n  ").append(columnDef).append(",");
            }
            System.out.println(createCommand);
        }
    }

    private static String getConstrains(Constraints con) {
        String constraints = "";
        if (!con.allowNull())
            constraints += " NOT NULL";
        if (!con.unique()) {
            constraints += " UNIQUE ";
        }
        return constraints;
    }
}
