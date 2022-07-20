package com.example.msharp;

import java.util.Map;
/*Equals expression. Covers "=="} and "!=" operators.*/
public class EqualExpression extends Expression{

    public Factor leftHandSide;
    public String operator;
    public Factor rightHandSide;
    Map<String,Integer> Numbers;
    Map<String, String> Strings;
    Map<String, String> Bools;
    public Boolean result;

    public  EqualExpression(Factor leftHandSide, String operator, Factor rightHandSide, Map<String,Integer> Numbers, Map<String, String> Strings, Map<String, String> Bools)
    {
        this.leftHandSide = leftHandSide;
        this.operator = operator;
        this.rightHandSide = rightHandSide;
        this.Numbers = Numbers;
        this.Strings = Strings;
        this.Bools = Bools;
    }


    @Override
    public void execute() throws Exception {
        Functions fun = new Functions();

        /*Get the values of the lhs and rhs and apply them based on the operator. */
        int lhs;
        int rhs;
        /*Evaluate the LHS factor. */
        if(fun.isInteger(leftHandSide.rawInput())) //is it an int?
        {
            lhs = Integer.parseInt(leftHandSide.rawInput());
        }
        else if(Numbers.containsKey(leftHandSide.rawInput())) //int var?
        {
            lhs = Numbers.get(leftHandSide.rawInput());
        }
        else //its a bool?
        {

                String[] split = leftHandSide.rawInput().split("!",2); //Work out if bool is notted.
                if(split[0].equals("")) // notted
                {
                    if(Bools.containsKey(split[1])) //variable
                    {
                        if(Bools.get(split[1]).equals("true"))
                        {
                            lhs = 0;
                        }

                        else
                        {
                            lhs = 1;
                        }
                    }
                    else if(fun.isBool(split[1]))   //bool literal.
                    {
                        if(split[1] == "true")
                        {
                            lhs = 0;
                        }

                        else
                        {
                            lhs = 1;
                        }
                    }
                    else //unknown variable
                    {
                        Exception e = new Exception("Variable does not exist ");
                        throw e;
                    }
                }
                else    //!notted
                {
                    if(Bools.containsKey(leftHandSide.rawInput())) //variable
                    {
                        if(Bools.get(leftHandSide.rawInput()).equals("true"))
                        {
                            lhs = 1;
                        }

                        else
                        {
                            lhs = 0;
                        }
                    }
                    else if(fun.isBool(leftHandSide.rawInput())) //bool literal
                    {
                        if(leftHandSide.rawInput().equals("true"))
                        {
                            lhs = 1;
                        }

                        else
                        {
                            lhs = 0;
                        }
                    }
                    else //unknown variable
                    {
                        Exception e = new Exception("Variable does not exist ");
                        throw e;
                    }
                }


        }
        /*Evaluate the RHS factor. */
        if(fun.isInteger(rightHandSide.rawInput())) //is it an int?
        {
            rhs = Integer.parseInt(rightHandSide.rawInput());
        }
        else if(Numbers.containsKey(rightHandSide.rawInput())) //int var?
        {
            rhs = Numbers.get(rightHandSide.rawInput());
        }
        else //its a bool
        {

            String[] split = rightHandSide.rawInput().split("!",2); //is it notted?
            if(split[0].equals("")) // notted
            {
                if(Bools.containsKey(split[1])) //is it a var?
                {
                    if(Bools.get(split[1]).equals("true"))
                    {
                        rhs = 0;
                    }

                    else
                    {
                        rhs = 1;
                    }
                }
                else if(fun.isBool(split[1])) //is it a bool literal?
                {
                    if(split[1].equals("true"))
                    {
                        rhs = 0;
                    }

                    else
                    {
                        rhs = 1;
                    }
                }
                else //unknown variable
                {
                    Exception e = new Exception("Variable does not exist ");
                    throw e;
                }
            }
            else    //not notted
            {
                if(Bools.containsKey(rightHandSide.rawInput())) //is it a var?
                {
                    if(Bools.get(rightHandSide.rawInput()).equals("true"))
                    {
                        rhs = 1;
                    }

                    else
                    {
                        rhs = 0;
                    }
                }
                else if(fun.isBool(rightHandSide.rawInput())) //is it a bool literal?
                {
                    if(rightHandSide.rawInput().equals("true"))
                    {
                        rhs = 1;
                    }

                    else
                    {
                        rhs = 0;
                    }
                }
                else //unknown variable
                {
                    Exception e = new Exception("Variable does not exist ");
                    throw e;
                }
            }


        }


        /*Evaluate expression based on operator. */
        switch (operator)
        {
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
    /*Return result. Will be asked for the bool result based on its type. */
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
