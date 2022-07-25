package com.example.msharp.factor;

import com.example.msharp.expression.ExpressionHelper;

/**
 * This class represents a factor of type integer e.g. 1, 2, 3, 4 etc.
 */
public class IntegerFactor extends Factor {
    public static final String TAG = "IntegerFactor";

    public int factor;

    public IntegerFactor(int factor) {
        this.factor = factor;
    }

    @Override
    public String rawInput() {
        return String.valueOf(factor);
    }

    @Override
    public void execute() {

    }

    @Override
    public int type() {
        return ExpressionHelper.intLit;
    }

    @Override
    public int resultInt() {
        return factor;
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
