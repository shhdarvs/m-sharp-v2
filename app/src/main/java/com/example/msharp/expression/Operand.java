package com.example.msharp.expression;

public class Operand <T> {
    public String type;
    public T value;

    public Operand(String type, T value) {
        this.type = type;
        this.value = value;
    }
}
