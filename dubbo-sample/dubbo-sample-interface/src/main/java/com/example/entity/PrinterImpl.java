package com.example.entity;

import com.example.entity.Printer;

public class PrinterImpl implements Printer {
    @Override
    public String print() {
        return "Hello!";
    }
}
