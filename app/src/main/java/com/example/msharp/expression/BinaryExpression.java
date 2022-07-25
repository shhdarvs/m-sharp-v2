package com.example.msharp.expression;

public class BinaryExpression extends Expression {
    public static final String TAG = "BinaryExpression";

    public String operator;
    public Expression e1, e2;       //left and right operands



    @Override
    public void execute() throws Exception {

    }

    @Override
    public int type() throws Exception {
        return 0;
    }

    @Override
    public int resultInt() {
        return 0;
    }

    @Override
    public double resultDouble() {
        return 0;
    }

    @Override
    public String resultBool() {
        return null;
    }

    @Override
    public String resultString() {
        return null;
    }
}
