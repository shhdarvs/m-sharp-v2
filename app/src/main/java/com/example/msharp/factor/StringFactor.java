package com.example.msharp.factor;

import com.example.msharp.expression.ExpressionHelper;

/**
 * This class represents a factor of type String
 */
public class StringFactor extends Factor {
    String factor;

    public StringFactor(String factor) {
        this.factor = factor;
    }

    @Override
    public void execute() {

    }

    @Override
    public int type() {
        return ExpressionHelper.stringLit;
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
        return factor;
    }

    @Override
    public String rawInput() {
        return null;
    }
}
