package com.example.msharp.expression;

import com.example.msharp.Functions;
import com.example.msharp.expression.Expression;
import com.example.msharp.expression.ExpressionHelper;
import com.example.msharp.factor.Factor;

import java.util.Map;
/*Equals expression. Covers "=="} and "!=" operators.*/

/**
 * --
 */
public class EqualExpression extends Expression {
    public static final String TAG = "EqualExpression";

    public Factor LHS;
    public String operator;
    public Factor RHS;
    Map<String, Integer> intMap;
    Map<String, Double> doubleMap;
    Map<String, String> stringMap;
    Map<String, String> boolMap;
    public boolean result;

    public EqualExpression(Factor LHS, String operator, Factor RHS, Map<String, Integer> intMap, Map<String, Double> doubleMap, Map<String, String> stringMap, Map<String, String> boolMap) {
        this.LHS = LHS;
        this.operator = operator;
        this.RHS = RHS;
        this.intMap = intMap;
        this.stringMap = stringMap;
        this.boolMap = boolMap;
    }


    @Override
    public void execute() throws Exception {
        Functions fun = new Functions();

        //Double values representing LHS and RHS
        double lhs;
        double rhs;

        String rawLHS = LHS.rawInput();
        String rawRHS = RHS.rawInput();

        //Evaluate LHS
        if (ExpressionHelper.isInteger(rawLHS)) //is it an int?
        {
            lhs = Integer.parseInt(LHS.rawInput());
        } else if (intMap.containsKey(LHS.rawInput())) //int var?
        {
            lhs = intMap.get(LHS.rawInput());
        } else //its a bool?
        {

            String[] split = LHS.rawInput().split("!", 2); //Work out if bool is notted.
            if (split[0].equals("")) // notted
            {
                if (boolMap.containsKey(split[1])) //variable
                {
                    if (boolMap.get(split[1]).equals("true")) {
                        lhs = 0;
                    } else {
                        lhs = 1;
                    }
                } else if (fun.isBool(split[1]))   //bool literal.
                {
                    if (split[1] == "true") {
                        lhs = 0;
                    } else {
                        lhs = 1;
                    }
                } else //unknown variable
                {
                    Exception e = new Exception("Variable does not exist ");
                    throw e;
                }
            } else    //!notted
            {
                if (boolMap.containsKey(LHS.rawInput())) //variable
                {
                    if (boolMap.get(LHS.rawInput()).equals("true")) {
                        lhs = 1;
                    } else {
                        lhs = 0;
                    }
                } else if (fun.isBool(LHS.rawInput())) //bool literal
                {
                    if (LHS.rawInput().equals("true")) {
                        lhs = 1;
                    } else {
                        lhs = 0;
                    }
                } else //unknown variable
                {
                    Exception e = new Exception("Variable does not exist ");
                    throw e;
                }
            }


        }
        /*Evaluate the RHS factor. */
        if (fun.isInteger(RHS.rawInput())) //is it an int?
        {
            rhs = Integer.parseInt(RHS.rawInput());
        } else if (intMap.containsKey(RHS.rawInput())) //int var?
        {
            rhs = intMap.get(RHS.rawInput());
        } else //its a bool
        {

            String[] split = RHS.rawInput().split("!", 2); //is it notted?
            if (split[0].equals("")) // notted
            {
                if (boolMap.containsKey(split[1])) //is it a var?
                {
                    if (boolMap.get(split[1]).equals("true")) {
                        rhs = 0;
                    } else {
                        rhs = 1;
                    }
                } else if (fun.isBool(split[1])) //is it a bool literal?
                {
                    if (split[1].equals("true")) {
                        rhs = 0;
                    } else {
                        rhs = 1;
                    }
                } else //unknown variable
                {
                    Exception e = new Exception("Variable does not exist ");
                    throw e;
                }
            } else    //not notted
            {
                if (boolMap.containsKey(RHS.rawInput())) //is it a var?
                {
                    if (boolMap.get(RHS.rawInput()).equals("true")) {
                        rhs = 1;
                    } else {
                        rhs = 0;
                    }
                } else if (fun.isBool(RHS.rawInput())) //is it a bool literal?
                {
                    if (RHS.rawInput().equals("true")) {
                        rhs = 1;
                    } else {
                        rhs = 0;
                    }
                } else //unknown variable
                {
                    Exception e = new Exception("Variable does not exist ");
                    throw e;
                }
            }


        }


        /*Evaluate expression based on operator. */
        switch (operator) {
            case "==":
                result = lhs == rhs;
                break;

            case "!=":
                result = lhs != rhs;
                break;

        }
    }

    /*Return type*/
    @Override
    public int type() {
        return 2;
    }

    @Override
    public int resultInt() {
        return 0;
    }

    @Override
    public double resultDouble() {
        return 0;
    }

    /*Return result. Will be asked for the bool result based on its type. */
    @Override
    public String resultBool() {
        if (result) {
            return "true";
        } else {
            return "false";
        }
    }

    @Override
    public String resultString() {
        return null;
    }
}
