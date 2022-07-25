package com.example.msharp.expression;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.msharp.factor.Factor;
import com.example.msharp.Logging;

import java.util.Map;
import java.util.Objects;

/**
 * This class is a helper class. There are static integer values which each represent a type of expression. There is also a helper method which executes and determines the type of an expression
 */
public class ExpressionHelper {
    public final static int addExp = 0;
    public final static int andExp = 1;
    public final static int eqlExp = 2;
    public final static int multExp = 3;
    public final static int relExp = 4;
    public final static int boolLit = 5;
    public final static int intLit = 6;
    public final static int stringLit = 7;
    public final static int varName = 8;
    public final static int doubleLit = 9;

    /**
     * This method executes and determines the type of an expression.
     *
     * @param TAG        The name of the Java class the method was invoked from
     * @param expression The expression to be evaluated
     * @return an integer value which represents the type of expression being returned
     */
    public static int executeAndGetType(String TAG, Expression expression) {
        try {
            expression.execute();
        } catch (Exception e) {
            Logging.error(TAG, e);
        }

        int type = -1;
        try {
            return expression.type();
        } catch (Exception e) {
            Logging.error(TAG, e);
        }

        return type;
    }

    /**
     * This method executes an expression.
     *
     * @param TAG        The name of the Java class the method was invoked from
     * @param expression The expression to be evaluated
     */
    public static void execute(String TAG, Expression expression) {
        try {
            expression.execute();
        } catch (Exception e) {
            Logging.error(TAG, e);
        }

    }

    /**
     * This method evaluates the condition of an integer expression and returns a result depending on the operand.
     *
     * @param LHS      the LHS of the expression
     * @param RHS      the RHS of the expression
     * @param operator the operator being applied to the expression
     * @return a boolean value indicating the result of the expression - true | false
     */
    public static boolean evaluateIntExpression(Factor LHS, Factor RHS, String operator) {
        int lhs = LHS.resultInt();
        ExpressionHelper.execute("", RHS);
        int rhs = RHS.resultInt();

        switch (operator) {
            case "==":
                return lhs == rhs;
            case "!=":
                return lhs != rhs;
            case "<":
                return lhs < rhs;
            case ">":
                return lhs > rhs;
            case "<=":
                return lhs <= rhs;
            case ">=":
                return lhs >= rhs;
            default:
                return false;
        }
    }

    /**
     * This method evaluates the condition of an boolean expression and returns a result depending on the operand.
     *
     * @param LHS      the LHS of the expression
     * @param RHS      the RHS of the expression
     * @param operator the operator being applied to the expression
     * @return a boolean value indicating the result of the expression - true | false
     */
    public static boolean evaluateBoolExpression(Factor LHS, Factor RHS, String operator) {
        String lhs = LHS.resultBool();
        ExpressionHelper.execute("", RHS);
        String rhs = RHS.resultBool();

        switch (operator) {
            case "==":
                return lhs.equals(rhs);
            case "!=":
                return !lhs.equals(rhs);
            default:
                return false;
        }
    }


    /**
     * This method evaluates whether or not a string represents a bool literal or a variable of type bool.
     *
     * @param input The string to be evaluated
     * @return an integer value representing the boolean value of the string: 0 = false, and 1 = true
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static int evaluateInputString(String TAG, String input, Map<String, String> boolMap) {
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

    public static boolean isInteger(String input) {
        try {
            int temp = Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDouble(String input) {
        try {
            double temp = Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * This method determines if a factor is a bool literal or a variable that has value of type bool
     *
     * @param input the string representation of the factor
     * @return true if the input is a bool literal, false if it is a variable
     */
    private static boolean isBool(String input) {
        return input.equals("true") || input.equals("false") || input.equals("!false") || input.equals("!true");
    }

}
