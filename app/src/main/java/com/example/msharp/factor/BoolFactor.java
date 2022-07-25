package com.example.msharp.factor;

import com.example.msharp.expression.ExpressionHelper;

/**
 * This class represents a factor of type boolean e.g. true or false
 */
public class BoolFactor extends Factor {
    public static final String TAG = "BoolFactor";

    public String factor;

    public BoolFactor(String factor) {
        this.factor = factor;
    }

    @Override
    public String rawInput() {
        return factor;
    }

    @Override
    public void execute() {
    }

    @Override
    public int type() {
        return ExpressionHelper.boolLit;
    }

    @Override
    public int resultInt() {
        return 0;
    }

    @Override
    public double resultDouble() {
        return 0;
    }


    /**
     * This method returns the value of the boolean literal. It is returned as a string value
     *
     * @return A string value representing the boolean value
     */
    @Override
    public String resultBool() {
        String[] split = factor.split("!", 2);

        //If the boolean value has been negated i.e !true or !false
        if (split[0].equals("")) {
            if (factor.equals("true")) {
                return "false";
            } else {
                return "true";
            }

        } else {
            return factor;
        }

    }

    @Override
    public String resultString() {
        return null;
    }
}
