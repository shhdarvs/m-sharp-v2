package com.example.msharp;

import com.example.msharp.statements.Statement;

import java.util.ArrayList;
import java.util.Map;

/* While statement */
public class WhileStatement extends Statement {

    public Factor leftHandSide;
    public String operator;
    public Factor rightHandSide;
    public ArrayList<Statement> codeBody;
    public Integer startIndex;
    public Map<String,Integer> Numbers;
    public Map<String, String> Strings;
    public Map<String, String> Bools;
    public Parser parser;

    public WhileStatement(Factor leftHandSide, String operator, Factor rightHandSide, ArrayList<Statement> codeBody, Integer startIndex /*,ArrayList<Statement> elseCodeBody*/, Map<String,Integer> Numbers, Map<String, String> Strings, Map<String, String> Bools, Parser parser)
    {
        this.leftHandSide = leftHandSide;
        this.operator = operator;
        this.rightHandSide = rightHandSide;
        this.codeBody = codeBody;
        this.startIndex = startIndex;
        this.Strings = Strings;
        this.Bools = Bools;
        this.Numbers = Numbers;
        this.parser = parser;
    }
    @Override
    public void execute() throws Exception {

        boolean isNumber = false;
        boolean result;

        /*Execute LHS*/
        leftHandSide.execute();

        /*Based on type, flip a bool on if its a Number or not. */
        switch (leftHandSide.type())
        {

            case 0:
            case 3:
            case 6:
                //number
                isNumber = true;
                break;
            case 1:
            case 2:
            case 4:
            case 5:
                //bool
                break;
            case 7:
                break;
        }
        /*Its a number. */
        if(isNumber)
        {
            /*Get the results of the LHS and RHS. */
            int lhs = leftHandSide.resultInt();
            rightHandSide.execute();
            int rhs = rightHandSide.resultInt();
            /*Switch based on the operator, execute the expression. Set the result*/
            switch (operator)
            {
                case "==":
                    if(lhs == rhs)
                    {
                        result = true;
                    }
                    else
                    {
                        result = false;
                    }
                    break;
                case "!=":
                    if(lhs != rhs)
                    {
                        result = true;
                    }
                    else
                    {
                        result = false;
                    }
                    break;
                case "<":
                    if(lhs < rhs)
                    {
                        result = true;
                    }
                    else
                    {
                        result = false;
                    }
                    break;
                case ">":
                    if(lhs > rhs)
                    {
                        result = true;
                    }
                    else
                    {
                        result = false;
                    }
                    break;
                case "<=":
                    if(lhs <= rhs)
                    {
                        result = true;
                    }
                    else
                    {
                        result = false;
                    }
                    break;
                case ">=":
                    if(lhs >= rhs)
                    {
                        result = true;
                    }
                    else
                    {
                        result = false;
                    }

                    break;
                default:
                    result = false;  //error
            }
            long start = System.currentTimeMillis();
            long end = start + 10000; // 60 seconds * 1000 ms/sec
            boolean running = true;

            while (System.currentTimeMillis() < end && running) //infinite loop recovery
            {

            /*While the result is true, carry out the code body. */
            while(result && System.currentTimeMillis() < end)
            {

                for(int n = 0; n < codeBody.size(); n++)
                {
                    Statement cur = codeBody.get(n);
                    cur.execute();
                }

                /*After the code body has done a loop, recalculate the condition to see if the loop continues. */
                lhs = leftHandSide.resultInt();
                rightHandSide.execute();
                rhs = rightHandSide.resultInt();

                switch (operator)
                {
                    case "==":
                        if(lhs == rhs)
                        {
                            result = true;
                        }
                        else
                        {
                            result = false;
                        }
                        break;
                    case "!=":
                        if(lhs != rhs)
                        {
                            result = true;
                        }
                        else
                        {
                            result = false;
                        }
                        break;
                    case "<":
                        if(lhs < rhs)
                        {
                            result = true;
                        }
                        else
                        {
                            result = false;
                        }
                        break;
                    case ">":
                        if(lhs > rhs)
                        {
                            result = true;
                        }
                        else
                        {
                            result = false;
                        }
                        break;
                    case "<=":
                        if(lhs <= rhs)
                        {
                            result = true;
                        }
                        else
                        {
                            result = false;
                        }
                        break;
                    case ">=":
                        if(lhs >= rhs)
                        {
                            result = true;
                        }
                        else
                        {
                            result = false;
                        }

                        break;
                    default:
                        result = false;  //error
                }
            }
            running = false;

            }
            if(result)
            {
                Exception e = new Exception("Infinite loop detected.");
                throw e;
            }
        }
        else //is bool
        {
            /*Get the results of the LHS and RHS. */
            String lhs = leftHandSide.resultBool();
            rightHandSide.execute();
            String rhs = rightHandSide.resultBool();

            /*Switch based on the operator, execute the expression. Set the result*/
            switch (operator)
            {
                case "==":
                    if(lhs.equals(rhs))
                    {
                        result = true;
                    }
                    else
                    {
                        result = false;
                    }
                    break;
                case "!=":
                    if(!lhs.equals(rhs))
                    {
                        result = true;
                    }
                    else
                    {
                        result = false;
                    }
                    break;
                default:
                    result = false; //error
                    break;

            }

            /*While the result is true, carry out the code body. */

            long start = System.currentTimeMillis();
            long end = start + 10000; // 60 seconds * 1000 ms/sec
            boolean running = true;

            while (System.currentTimeMillis() < end && running) {   //infinite loop recovery
                while (result && System.currentTimeMillis() < end) {
                    //do if
                    for (int n = 0; n < codeBody.size(); n++) {
                        Statement cur = codeBody.get(n);
                        cur.execute();
                    }

                    lhs = leftHandSide.resultBool();
                    rightHandSide.execute();
                    rhs = rightHandSide.resultBool();

                    switch (operator) {
                        case "==":
                            if (lhs.equals(rhs)) {
                                result = true;
                            } else {
                                result = false;
                            }
                            break;
                        case "!=":
                            if (!lhs.equals(rhs)) {
                                result = true;
                            } else {
                                result = false;
                            }
                            break;
                        default:
                            result = false; //error
                            break;

                    }
                }
                running = false;
            }

            if(result)
            {
                Exception e = new Exception("Infinite loop detected.");
                throw e;
            }
        }

    }
}
