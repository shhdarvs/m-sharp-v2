package com.example.msharp.expression;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.msharp.factor.Factor;
import com.example.msharp.Logging;

import java.util.Map;
import java.util.Objects;


/**
 * This class represents a logical expression. This expression can contain either the AND (&&) or the OR (||) operation between elements. This class definition can also be used for unary expressions
 */
public class AndExpression extends Expression {
    public static final String TAG = "AndExpression";

    public Factor LHS;
    public String operator;
    public Factor RHS;
    public Map<String, String> boolMap;
    public boolean result;

    public AndExpression(Factor LHS, String operator, Factor RHS, Map<String, String> boolMap) {
        this.LHS = LHS;
        this.operator = operator;
        this.RHS = RHS;
        this.boolMap = boolMap;
    }


    /**
     * This method determines if a factor is a bool literal or a variable that has value of type bool
     *
     * @param input the string representation of the factor
     * @return true if the input is a bool literal, false if it is a variable
     */
    public static boolean isBool(String input) {
        return input.equals("true") || input.equals("false") || input.equals("!false") || input.equals("!true");
    }


    /**
     * This method applies the boolean operator to the LHS and RHS of the expression, and returns its result
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void execute() {
        //Integer representations of the LHS and RHS : 0 = false ; 1 = true
        int lhs = -1;
        int rhs = -1;

        boolean negL = false;
        boolean negR = false;

        String rawLHS = LHS.rawInput();
        String rawRHS = RHS.rawInput();

        //Evaluate the boolean value of each side of the expression
        lhs = evaluateInputString(rawLHS);
        rhs = evaluateInputString(rawRHS);

        //Switch on the type of operator
        switch (operator) {
            case "&&":
                result = lhs == 1 && rhs == 1;
                break;

            case "||":
                result = lhs == 1 || rhs == 1;
                break;

        }
    }


    /**
     * This method evaluates whether or not a string represents a bool literal or a variable of type bool.
     *
     * @param input The string to be evaluated
     * @return an integer value representing the boolean value of the string: 0 = false, and 1 = true
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private int evaluateInputString(String input) {
        int val = -1;
        boolean isNeg = false;
        String raw = "";

        if (input.charAt(0) == '!') {
            isNeg = true;
            input = input.substring(2);
        }

        //Is it a variable?
        if (boolMap.containsKey(input)) {
            if (Objects.equals(boolMap.get(input), "false"))
                val = 0;
            else
                val = 1;

        }

        //else its a bool literal.
        else if (isBool(input)) {
            if (input.equals("false")) {
                val = 0;
            } else {
                val = 1;
            }
        } else
            Logging.error(TAG, new Exception("Variable does not exist "));

        if (isNeg)
            val ^= 1;

        return val;
    }

    @Override
    public int type() {
        return ExpressionHelper.andExp;
    }

    @Override
    public int resultInt() {
        return 0;
    }

    @Override
    public double resultDouble() {
        return 0.0;
    }

    @Override
    public String resultBool() {
        if (result)
            return "true";
        else
            return "false";
    }

    @Override
    public String resultString() {
        return null;
    }
}
