package com.example.msharp;

import com.google.android.material.navigation.NavigationView;

import java.util.Map;

/* Relationship expressions:
Covers cases where one element is compared to another using any of the following operators:
"<", ">", "<=" or ">=" */
public class RelationExpression extends Expression{

    public Factor leftHandSide;
    public String operator;
    public Factor rightHandSide;
    Map<String, Integer> Numbers;
    public boolean result;

    public  RelationExpression(Factor leftHandSide, String operator, Factor rightHandSide, Map<String, Integer> Numbers)
    {
        this.leftHandSide = leftHandSide;
        this.operator = operator;
        this.rightHandSide = rightHandSide;
        this.Numbers = Numbers;
    }



    /*Get the values of the lhs and rhs and apply them based on the operator. */
    @Override
    public void execute() throws Exception {
        Functions fun = new Functions();
        int rhs;
        int lhs;
        /*Evaluate LHS factor. */
        if(fun.isInteger(this.leftHandSide.rawInput())) //is it an int?
        {
            lhs = Integer.parseInt(leftHandSide.rawInput());
        }
        else if(Numbers.containsKey(leftHandSide.rawInput())) //is it a var?
        {
            lhs = Numbers.get(leftHandSide.rawInput());
        }
        else //unknown variable
        {
            Exception e = new Exception("Variable does not exist ");
            throw e;
        }
        /*Evaluate RHS factor. */
        if(fun.isInteger(this.rightHandSide.rawInput())) //is it an int?
        {
            rhs = Integer.parseInt(rightHandSide.rawInput());
        }
        else if(Numbers.containsKey(rightHandSide.rawInput())) //is it a var?
        {
            rhs = Numbers.get(rightHandSide.rawInput());
        }
        else //unknown variable
        {
            Exception e = new Exception("Variable does not exist ");
            throw e;
        }

        /*Evaluate expression based on operator. */
        switch(operator)
        {
            case ">":

                this.result = lhs > rhs;
                break;

            case "<":

                this.result = lhs < rhs;
                break;

            case ">=":

                this.result = lhs >= rhs;
                break;

            case "<=":

                this.result = lhs <= rhs;
                break;
        }

    }

    /*Get type of expression. */
    @Override
    public int type() {
        return 4;
    }

    @Override
    public int resultInt() {
        return 0;
    }

    /*Based on its type, will be asked for its result. In this case, bool result. */
    @Override
    public String resultBool() {
        if(result){
            return "true";
        }
        else
        {
            return "false";
        }
    }

    @Override
    public String resultString() {
        return null;
    }
}
