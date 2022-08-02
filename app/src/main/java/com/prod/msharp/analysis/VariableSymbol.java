package com.prod.msharp.analysis;

import java.lang.reflect.Type;

public class VariableSymbol {
    public String name;
    public Type type;

    public VariableSymbol(String name, Type type) {
        this.name = name;
        this.type = type;
    }
}
