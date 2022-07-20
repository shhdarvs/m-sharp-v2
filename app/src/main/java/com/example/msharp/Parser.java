//import com.sun.org.apache.xpath.internal.operations.Bool;
package com.example.msharp;


import android.content.Context;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Parser {
    TextView console; //The view the interpreter outputs to.
    Context context;

    public Parser(TextView console, Context context)
    {
        this.console = console;
        this.context = context;
    }

    /* Function to check if given input starts with '#', making it a string. */
    public static boolean isString(String input)
    {
        char[] allChars = input.toCharArray();
        if(allChars[0] == '#')
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /*Parse tokens for the lhs factor of an expression. */
    public static Factor parseLHSFactor(String[] expression, Map<String, Integer> Numbers, Map<String, String> Strings, Map<String, String> Bools)
    {
        Factor factor;

        if(isBool(expression[0]))
        {
            factor = new BoolFactor(expression[0]);
        }
        else if(isInteger(expression[0]))
        {
            factor = new IntegerFactor(Integer.parseInt(expression[0]));
        }
        else
        {
            factor = new VariableFactor(expression[0], Numbers, Strings, Bools);
        }

        return factor;
    }

    /*Parse tokens for the rhs factor of an expression. */
    public static Factor parseRHSFactor(String[] expression, Map<String, Integer> Numbers, Map<String, String> Strings, Map<String, String> Bools)
    {
        Factor factor;

        if(isBool(expression[2]))
        {
            factor = new BoolFactor(expression[2]);
        }
        else if(isInteger(expression[2]))
        {
            factor = new IntegerFactor(Integer.parseInt(expression[2]));
        }
        else
        {
            factor = new VariableFactor(expression[2], Numbers, Strings, Bools);
        }

        return factor;
    }

    /*Check to see if a token is an int. */
    public static boolean isInteger(String input)
    {
        try
        {
            int temp = Integer.parseInt(input);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    /*Check to see if a token is a bool. */
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

    /*A check to see if an expression is a single factor or two factors with an operator. */
    public static boolean isExpressionWithOperator (String input)
    {
        String[] splitInput = input.split(" ",3 );
        if(splitInput.length == 3)
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    /*Function that takes a string and processes it into an expression.  */
    public static Expression processExpression (String input,  Map<String, Integer> Numbers, Map<String, String> Strings, Map<String, String> Bools)
    {

        String[] expression = input.split(" ",3 );
        Factor lhs;
        Factor rhs;
        String op = expression[1];
        /*Parse lhs and rhs factors. */
        lhs = parseLHSFactor(expression, Numbers, Strings, Bools);

        rhs = parseRHSFactor(expression, Numbers, Strings, Bools);

        /*Switch based on operator, parse the expression as the appropriate object. */
        switch(op)
        {
            case "+":
                AddExpression newAddExp = new AddExpression(lhs, "+",rhs, Numbers);
                return newAddExp;

            case "-":
                AddExpression newMinusExp = new AddExpression(lhs, "-",rhs, Numbers);
                return  newMinusExp;

            case "*":
                MultExpression newTimesExp = new MultExpression(lhs, "*",rhs, Numbers, Strings, Bools);
                return  newTimesExp;

            case "/":
                MultExpression newDivideExp = new MultExpression(lhs, "/",rhs, Numbers, Strings, Bools);
                return newDivideExp;

            case "%":
                MultExpression newModExp = new MultExpression(lhs, "%", rhs, Numbers, Strings, Bools);
                return  newModExp;
            case "^":
                MultExpression newPowExp = new MultExpression(lhs, "^", rhs, Numbers, Strings, Bools);
                return  newPowExp;
            case "<":
                RelationExpression newLT = new RelationExpression(lhs, "<",rhs, Numbers);
                return  newLT;
            case ">":
                RelationExpression newGT = new RelationExpression(lhs, ">",rhs, Numbers);
                return newGT;
            case "<=":
                RelationExpression newLTE = new RelationExpression(lhs,"<=",rhs, Numbers);
                return newLTE;
            case ">=":
                RelationExpression newGTE = new RelationExpression(lhs,">=",rhs, Numbers);
                return  newGTE;
            case "==":
                EqualExpression newEq = new EqualExpression(lhs, "==", rhs, Numbers, Strings, Bools);
                return newEq;
            case "!=":
                EqualExpression newNe = new EqualExpression(lhs, "!=", rhs, Numbers, Strings, Bools);
                return  newNe;
            case "&&":
                AndExpression newAa = new AndExpression(lhs, "&&", rhs, Bools);
                return  newAa;
            case "||":
                AndExpression newOo = new AndExpression(lhs, "||", rhs, Bools);
                return  newOo;
        }
        return null;
    }

    /* The parser. Reads raw code and transforms it into an AST. */
    public void ParseTokens(String Tokens[], String line, Map<String, Integer> Numbers, Map<String, String> Strings, Map<String, String> Bools, Program program, int currentLine, ArrayList<String> rawCode) throws Exception {
            /* Tokens[0] is the first token on a line of code.
            It tells the parser what type of statement to expect. */
            switch (Tokens[0])
            {
                /* Assignment Statement */
                case "let": /* Examples: let x = 1 + 1, let x = 17, let x = #hello, let x = y, let x = y + 1, let x = true, let x = y < 4*/

                    /* Separate the expression from the variable name and "=" sign. ie: {"x = ","y + 1"} */
                    String[] varAndExpression = line.split("= ",2 );

                    /* Our new variable will be read from the console. ie: let x = read*/
                    if(varAndExpression[1].equals("read"))
                    {
                        ReadInExpression newExpression = new ReadInExpression(context);
                        AssignmentStatement newAssignmentStatement = new AssignmentStatement(Tokens[1], newExpression,Numbers,Strings, Bools);
                        program.Add(newAssignmentStatement);
                    }

                    /* Our new variable is a string. ie: let x = #how now brown cow */
                    else if(isString(varAndExpression[1]))
                    {
                        StringFactor newFactor = new StringFactor(varAndExpression[1]);
                        AssignmentStatement newAssignmentStatement = new AssignmentStatement(Tokens[1], newFactor, Numbers, Strings, Bools);
                        program.Add(newAssignmentStatement);
                    }

                    /* Our new variable is an int. ie: let x = 77 */
                    else if(isInteger(varAndExpression[1]))
                    {
                        IntegerFactor newFactor = new IntegerFactor(Integer.parseInt(varAndExpression[1]));
                        AssignmentStatement newAssignmentStatement = new AssignmentStatement(Tokens[1], newFactor, Numbers, Strings, Bools);
                        program.Add(newAssignmentStatement);
                    }
                    /* Our new variable is a boolean. ie: let x = true */
                    else if (isBool(varAndExpression[1]))
                    {
                        BoolFactor newBoolFactor = new BoolFactor(varAndExpression[1]);
                        AssignmentStatement newAssignmentStatement = new AssignmentStatement(Tokens[1], newBoolFactor, Numbers, Strings, Bools);
                        program.Add(newAssignmentStatement);
                    }

                    /* Our new variable is copying an existing variable. ie: let x = y */


                    /* Our new variable has an expression that needs to be evaluated first. ie let x = y + 1,let x = y != 3 */
                    else if(isExpressionWithOperator(varAndExpression[1]))
                    {
                        Expression newExpression = processExpression(varAndExpression[1], Numbers, Strings, Bools);
                        AssignmentStatement newAssignmentStatement = new AssignmentStatement((Tokens[1]), newExpression, Numbers, Strings, Bools);
                        program.Add(newAssignmentStatement);
                    }
                    else /* if(isExistingVariable(varAndExpression[1], Numbers, Strings, Bools)) */
                    {
                        VariableFactor newVariableFactor = new VariableFactor(varAndExpression[1], Numbers, Strings, Bools);
                        AssignmentStatement newAssignmentStatement_5 = new AssignmentStatement(Tokens[1], newVariableFactor, Numbers, Strings, Bools);
                        program.Add(newAssignmentStatement_5);
                    }
                    break;

                /* Console Output Statement */
                case "print":

                    String[] printAndExpression = line.split(" ",2 );

                    /* Printing string */
                    if(isString(printAndExpression[1]))
                    {
                        StringFactor newStringFactor = new StringFactor(printAndExpression[1]);
                        ConsoleOutStatement newConsoleOutStatement_1 = new ConsoleOutStatement(newStringFactor, Numbers, Strings, Bools, console);
                        program.Add(newConsoleOutStatement_1);
                    }

                    /* Printing an int */
                    else if(isInteger(printAndExpression[1]))
                    {
                        IntegerFactor newIntegerFactor = new IntegerFactor(Integer.parseInt(printAndExpression[1]));
                        ConsoleOutStatement newConsoleOutStatement_2 = new ConsoleOutStatement(newIntegerFactor, Numbers, Strings, Bools, console);
                        program.Add(newConsoleOutStatement_2);
                    }

                    /* Printing a bool */
                    else if(isBool(printAndExpression[1]))
                    {
                        BoolFactor newBoolFactor = new BoolFactor(printAndExpression[1]);
                        ConsoleOutStatement newConsoleOutStatement_2_1 = new ConsoleOutStatement(newBoolFactor, Numbers, Strings, Bools, console);
                        program.Add(newConsoleOutStatement_2_1);
                    }

                    /* Printing the evaluation of an expression. */
                    else if(isExpressionWithOperator(printAndExpression[1]))
                    {
                        Expression newExpression = processExpression(printAndExpression[1], Numbers, Strings, Bools);
                        ConsoleOutStatement newConsoleOutStatement_4 = new ConsoleOutStatement(newExpression, Numbers, Strings, Bools, console);
                        program.Add(newConsoleOutStatement_4);
                    }

                    /* Printing the value of an existing variable. */
                    else
                    {
                        VariableFactor newVariableFactor = new VariableFactor(printAndExpression[1], Numbers, Strings, Bools);
                        ConsoleOutStatement newConsoleOutStatement_3 = new ConsoleOutStatement(newVariableFactor, Numbers, Strings, Bools, console);
                        program.Add(newConsoleOutStatement_3);
                    }
                    break;


                /* If Statement */
                case "if":

                    Factor ifLhs;
                    Factor ifRhs;
                    String ifOp = "";
                    int ifStart = currentLine + 1;

                    /*We know this string can be parsed as expected, so we take off the "if":*/
                    String[] ifAndExpression = line.split(" ",2 );

                    /*Now we split the string into its 3 tokens. The lhs factor, operator and rhs factor. */
                    String[] expression = ifAndExpression[1].split(" ",3 );

                    /*Now we parse the tokens to retrieve our LHS and RHS factors, returning objects of type Factor.*/
                    ifLhs = parseLHSFactor(expression, Numbers, Strings, Bools);

                    ifRhs = parseRHSFactor(expression, Numbers, Strings, Bools);

                    /*Store a reference to the operator string for later. */
                    ifOp = expression[1];

                    /*Prep the variables needed to record the body of code that falls between the if statement and the ifEnd statement.*/
                    String codeBodyLine;
                    /*Catch unclosed scope errors*/
                    try
                    {
                        codeBodyLine = rawCode.get(currentLine + 1); //Get the next line
                    }
                    catch(IndexOutOfBoundsException e)
                    {
                        Exception ex = new Exception("Unclosed scope");
                        throw ex;
                    }
                    currentLine++;
                    String codeBodyLineTokens[] = codeBodyLine.split(" ");
                    /*Create a list for the lines of code to be stored in.*/
                    List<String> codeBody = new ArrayList<String>();


                    /* Get the code to be executed under the condition. */
                    int ifCount = 0; //Counter used for finding nested if's. if counter is incremented, we know the next ifEnd we find is not our ifEnd.
                    while(!codeBodyLineTokens[0].equals("ifEnd") || ifCount != 0)
                    {
                        if(codeBodyLineTokens[0].equals("if")) //Nested if statement found.
                        {
                            ifCount++;
                        }
                        if(codeBodyLineTokens[0].equals("ifEnd")) //End of nested if statement.
                        {
                            ifCount--;
                        }
                        codeBody.add(codeBodyLine); //Add the line of code to our code body.
                        /*Catch unclosed scope errors*/
                        try
                        {
                            codeBodyLine = rawCode.get(currentLine + 1); //get the next line.
                        }

                            catch(IndexOutOfBoundsException e)
                        {
                            Exception ex = new Exception("Unclosed scope");
                            throw ex;
                        }
                        currentLine++; //increase current line so it doens't get parsed twice.
                        codeBodyLineTokens = codeBodyLine.split(" "); //process line into its tokens, so we can check for "if" and "ifEnd" tokens.
                    }

                    /*The code within an if statement is parsed as a program. We now set up the variables we need to accomplish this.*/
                    Program ifTempProgram = new Program();
                    Boolean needToSkipIf_if = false;
                    Boolean needToSkipWhile_if = false;

                    /*Now we parse the body of code that falls between our "if" and "ifEnd". Nested statements will be parsed and take care of parsing their own respective code.*/
                    for(int x = 0; x < codeBody.size(); x++)
                    {
                        String temp = codeBody.get(x);
                        String[] tempTokens = temp.split(" ");

                        /*These if statements determine if the code needs to be parsed by this "if", or if its part of a nested statement which will parse it itself.*/
                        if(!needToSkipIf_if && !needToSkipWhile_if)
                        {
                            ParseTokens(tempTokens, temp, Numbers, Strings, Bools, ifTempProgram, ifStart + x, rawCode);
                        }
                        if(tempTokens[0].equals("ifEnd"))
                        {
                            needToSkipIf_if = false;
                        }
                        if(tempTokens[0].equals("whileEnd"))
                        {
                            needToSkipWhile_if = false;
                        }
                        if(tempTokens[0].equals("if"))
                        {
                            needToSkipIf_if = true;
                        }
                        if(tempTokens[0].equals("while"))
                        {
                            needToSkipWhile_if = true;
                        }

                    }
                    /*Create new ifStatement object.*/
                    IfStatement newIfStatement = new IfStatement(ifLhs, ifOp, ifRhs, ifTempProgram.statements , currentLine, Numbers, Strings, Bools, this);
                    /*Add the new ifStatement to our AST*/
                    program.Add(newIfStatement);
                    break;

                /* While statement */
                case "while":

                    Factor whileLhs;
                    Factor whileRhs;
                    String whileOp = "";
                    int whileStart = currentLine + 1;

                    /*We know this string can be parsed as expected, so we take off the "while":*/
                    String[] whileAndExpression = line.split(" ",2 );

                    /*Now we split the string into its 3 tokens. The lhs factor, operator and rhs factor. */
                    String[] whileExpression = whileAndExpression[1].split(" ",3 );


                    /*Now we parse the tokens to retrieve our LHS and RHS factors, returning objects of type Factor.*/
                    whileLhs = parseLHSFactor(whileExpression, Numbers, Strings, Bools);

                    whileRhs = parseRHSFactor(whileExpression, Numbers, Strings, Bools);

                    /*Store a reference to the operator string for later. */
                    whileOp = whileExpression[1];


                    /*Prep the variables needed to record the body of code that falls between the while statement and the whileEnd statement.*/

                    String codeBodyLine_while;

                    try {
                        codeBodyLine_while = rawCode.get(currentLine + 1); //get the next line.
                    }
                    catch(IndexOutOfBoundsException e)
                    {
                        Exception ex = new Exception("Unclosed scope");
                        throw ex;
                    }

                    currentLine++;
                    String codeBodyLineTokens_while[] = codeBodyLine_while.split(" ");
                    List<String> codeBody_while = new ArrayList<String>();

                    /* Get the code to be executed under the condition. */
                    int whileCount = 0;//Counter used for finding nested while's. if counter is incremented, we know the next whileEnd we find is not our whileEnd.
                    while(!codeBodyLineTokens_while[0].equals("whileEnd") || whileCount != 0)
                    {
                        if(codeBodyLineTokens_while[0].equals("while"))//Nested while statement found.
                        {
                            whileCount++;
                        }
                        if(codeBodyLineTokens_while[0].equals("whileEnd"))//End of nested while statement.
                        {
                            whileCount--;
                        }
                        codeBody_while.add(codeBodyLine_while); //Add the line of code to our code body.
                        try {
                            codeBodyLine_while = rawCode.get(currentLine + 1); //get the next line.
                        }
                        catch(IndexOutOfBoundsException e)
                        {
                            Exception ex = new Exception("Unclosed scope");
                            throw ex;
                        }
                        currentLine++; //increase current line so it doens't get parsed twice.
                        codeBodyLineTokens_while = codeBodyLine_while.split(" "); //process line into its tokens, so we can check for "while" and "whileEnd" tokens.

                    }

                    /*The code within an while statement is parsed as a program. We now set up the variables we need to accomplish this.*/
                    Program whileTempProgram = new Program();
                    Boolean needToSkipIf_while = false;
                    Boolean needToSkipWhile_while = false;

                    for(int x = 0; x < codeBody_while.size(); x++)
                    {

                        String temp = codeBody_while.get(x);
                        String[] tempTokens = temp.split(" ");

                        if(!needToSkipIf_while && !needToSkipWhile_while)
                        {
                            ParseTokens(tempTokens, temp, Numbers, Strings, Bools, whileTempProgram, whileStart + x, rawCode);
                        }
                        if(tempTokens[0].equals("ifEnd"))
                        {
                            needToSkipIf_while = false;
                        }
                        if(tempTokens[0].equals("whileEnd"))
                        {

                            needToSkipWhile_while = false;

                        }
                        if(tempTokens[0].equals("if"))
                        {
                            needToSkipIf_while = true;
                        }
                        if(tempTokens[0].equals("while"))
                        {
                            needToSkipWhile_while = true;

                        }


                    }
                    /*Create new whileStatement object.*/
                    WhileStatement newWhileStatement = new WhileStatement(whileLhs, whileOp, whileRhs, whileTempProgram.statements, currentLine, Numbers, Strings, Bools, this);
                    /*Add the new whileStatement to our AST*/
                    program.Add(newWhileStatement);
                    break;

                case "":
                    break;

                default:
                    /* Can't get here */

            }
        }




}
