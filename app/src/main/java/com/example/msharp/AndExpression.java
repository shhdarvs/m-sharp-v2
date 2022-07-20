package com.example.msharp;

import java.util.Map;

/* And expressions: Covers both AND ("&&") and OR ("||") operations between two elements. ie: true || false, a && b. */
public class AndExpression extends  Expression
{
    public Factor leftHandSide;
    public String operator;
    public Factor rightHandSide;
    public Map<String,String> Bools;
    public boolean result;

    /*Constructor*/
    public  AndExpression(Factor leftHandSide, String operator, Factor rightHandSide, Map<String,String> Bools)
    {
        this.leftHandSide = leftHandSide;
        this.operator = operator;
        this.rightHandSide = rightHandSide;
        this.Bools = Bools;
    }
    /*A method to determine if a factor is a bool literal. The alternative is it's a variable.*/
    public static boolean isBool(String input)
    {
        if(input.equals("true") || input.equals("false") || input.equals("!false") || input.equals("!true"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /*Get the values of the lhs and rhs and apply them based on the operator. */
    @Override
    public void execute() throws Exception {
        Functions fun = new Functions();

        int lhs;
        int rhs;

            String[] split = leftHandSide.rawInput().split("!",2); //Checking if the factor is notted.
            if(split[0].equals("")) //Factor is notted.
            {
                if(Bools.containsKey(split[1])) //Is it a variable?
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
                else if(isBool(split[1]))   //else its a bool literal.
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
            else    //Factor is not notted.
            {
                if(Bools.containsKey(leftHandSide.rawInput())) //Is it a variable?
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
                else if(isBool(leftHandSide.rawInput())) //else its a bool literal.
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

            /*Same procedure as above, now for the right hand side factor. */
            String[] split2 = rightHandSide.rawInput().split("!",2); //Check to see if its notted.
            if(split2[0].equals("")) // notted
            {
                if(Bools.containsKey(split2[1])) //is it a variable?
                {
                    if(Bools.get(split2[1]).equals("true"))
                    {
                        rhs = 0;
                    }

                    else
                    {
                        rhs = 1;
                    }
                }
                else if(isBool(split2[1])) //is it a bool literal?
                {
                    if(split2[1].equals("true"))
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
            else    //!notted
            {
                if(Bools.containsKey(rightHandSide.rawInput())) //is it a variable?
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
                else if(isBool(rightHandSide.rawInput())) //is it a bool lit?
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




        /*Produce result based on what the operator is.*/
        switch (operator)
        {
            case "&&":
                if(lhs == 1 && rhs == 1)
                {
                    result = true;
                }
                else
                {
                    result = false;
                }
                break;

            case "||":
                if(lhs == 1 || rhs == 1)
                {
                    result = true;
                }
                else
                {
                    result = false;
                }
                break;

        }
    }

    @Override
    public int type() {
        return 1;
    }

    @Override
    public int resultInt() {
        return 0;
    }

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
