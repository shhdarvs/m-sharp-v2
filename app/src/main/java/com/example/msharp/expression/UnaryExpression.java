package com.example.msharp.expression;

/**
 * This class represents an expression that is unary, i.e. has one operands preceded by an operator
 */
public class UnaryExpression extends Expression{
    public static final String TAG = "UnaryExpression";

    public String operator;
    public Expression e;

    public UnaryExpression(String operator, Expression e) {
        this.operator = operator;
        this.e = e;
    }


    @Override
    public void execute() throws Exception {

    }

    @Override
    public int type() throws Exception {
        return ExpressionHelper.unExp;
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
