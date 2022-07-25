package com.example.msharp.factor;

import com.example.msharp.expression.ExpressionHelper;

/**
 * This class represents a factor of type double e.g. 4.6, 7.5, 1.265 etc.
 */
public class DoubleFactor extends Factor {
    public static final String TAG = "IntegerFactor";

    public Double factor;

    public DoubleFactor(double element) {
        this.factor = element;
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
        return ExpressionHelper.doubleLit;
    }

    @Override
    public int resultInt() {
        return 0;
    }

    @Override
    public double resultDouble() {
        return factor;
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
