package com.example.msharp.expression;

import android.util.Log;

import com.example.msharp.Factor;
import com.example.msharp.Functions;
import com.example.msharp.Logging;
import com.example.msharp.expression.Expression;
import com.example.msharp.expression.ExpressionHelper;

import java.util.Map;

/*Add expression: Represents the addition and subtraction of two integers. ie: 1 + 2, 7 - a (given a is an int).*/

/**
 * This class represents an addition or subtraction expression. The addition and subtraction is binary, but unary expressions can also be defined using this class definition
 */
public class AddExpression extends Expression {
    public static final String TAG = "AddExpression";

    public Factor LHS;
    public String operator;
    public Factor RHS;
    public Map<String, Integer> intMap;
    public Map<String, Double> doubleMap;
    public double result;
    public int type; //does the addition or subtraction result in a double or int


    public AddExpression(Factor LHS, String operator, Factor RHS, Map<String, Integer> intMap, Map<String, Double> doubleMap) {
        this.LHS = LHS;
        this.operator = operator;
        this.RHS = RHS;
        this.intMap = intMap;
        this.doubleMap = doubleMap;
    }

    /**
     * This method performs an addition or subtraction on two expressions. The expressions are either both int literals, both double literals or the LHS is an int liter and the RHS is a double literal
     */
    @Override
    public void execute() {
        double lhs = -1;
        double rhs = -1;

        //The String representations of the left and right hand side of the expressions
        String LHSInput = LHS.rawInput();
        String RHSInput = LHS.rawInput();

        //If the LHS is an integer
        if (Functions.isInteger(LHSInput))
            lhs = Integer.parseInt(LHSInput);
        else if (Functions.isDouble(LHSInput)) //If the LHS is a double
            lhs = Double.parseDouble(LHSInput);
        else { //The LHS is a variable and is either contained in the int variable reference map or the double variable reference map
            if (intMap.containsKey(LHSInput))
                lhs = intMap.get(LHSInput);
            else if (doubleMap.containsKey(LHSInput))
                lhs = doubleMap.get(LHSInput);
            else
                Logging.error(TAG, new Exception("Variable does not exist"));

        }

        //If the RHS is an integer
        if (Functions.isInteger(RHSInput))
            lhs = Integer.parseInt(RHSInput);
        else if (Functions.isDouble(RHSInput)) //If the RHS is a double
            lhs = Double.parseDouble(RHSInput);
        else { //The RHS is a variable and is either contained in the int variable reference map or the double variable reference map
            if (intMap.containsKey(RHSInput))
                lhs = intMap.get(RHSInput);
            else if (doubleMap.containsKey(RHSInput))
                lhs = doubleMap.get(RHSInput);
            else
                Logging.error(TAG, new Exception("Variable does not exist"));

            switch (operator) //Determine if it is an addition or subtraction.
            {
                case "+":
                    this.result = lhs + rhs;
                    break;
                case "-":

                    this.result = lhs - rhs;
                    break;
            }

        }

    }

    @Override
    public int type() {
        return ExpressionHelper.addExp;
    }

    @Override
    public int resultInt() {
        return (int) result;
    }

    @Override
    public double resultDouble() {
        return result;
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
