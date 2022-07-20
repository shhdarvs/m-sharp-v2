package com.example.msharp.expression;

import com.example.msharp.AST;

public abstract class Expression extends AST {
    public abstract void execute() throws Exception;
    public abstract int type() throws Exception;
    public abstract int resultInt();
    public abstract int resultDouble();
    public abstract String resultBool();
    public abstract String resultString();
}
