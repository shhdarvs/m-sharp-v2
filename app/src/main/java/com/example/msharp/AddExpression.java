package com.example.msharp;

import com.google.android.material.navigation.NavigationView;

import java.util.Map;

/*Add expression: Represents the addition and subtraction of two integers. ie: 1 + 2, 7 - a (given a is an int).*/
public class AddExpression extends Expression{
    public Factor leftHandSide;
    public String operator;
    public Factor rightHandSide;
    public Map<String, Integer> Numbers;
    public int result;

    /*Constructor*/
    public AddExpression(Factor leftHandSide, String operator, Factor rightHandSide, Map<String, Integer> Numbers)
    {
        this.leftHandSide = leftHandSide;
        this.operator = operator;
        this.rightHandSide = rightHandSide;
        this.Numbers = Numbers;
    }


    /*Called to evaluate the result of the integer.*/
    @Override
    public void execute() throws Exception {
        Functions fun = new Functions();

        int rhs;
        int lhs;
        if(fun.isInteger(this.leftHandSide.rawInput())) //if it is an int literal
        {
            lhs = Integer.parseInt(leftHandSide.rawInput());
        }
        else
        {
            if(Numbers.containsKey(leftHandSide.rawInput())) //else check if its a declared variable.
            {
                lhs = Numbers.get(leftHandSide.rawInput());
            }
            else
            {
                Exception e = new Exception("Variable does not exist"); //if not, throw an error.
                throw e;
            }
        }
        if(fun.isInteger(this.rightHandSide.rawInput()))    //if it is an int literal
        {
            rhs = Integer.parseInt(rightHandSide.rawInput());
        }
        else
        {
            if(Numbers.containsKey(rightHandSide.rawInput())) //else check if its a declared variable.
            {
                rhs = Numbers.get(rightHandSide.rawInput());
            }
            else
            {
                Exception e = new Exception("Variable does not exist"); //if not, throw an error.
                throw e;
            }
        }

        switch(operator) //Determine if it is an addition or subtraction.
        {
            case "+":

                this.result = rhs + lhs;
                break;
            case "-":

                this.result = lhs - rhs;
                break;
        }

    }

    @Override
    public int type() {
        return 0;
    }

    @Override
    public int resultInt() {
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
