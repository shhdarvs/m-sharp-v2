package com.example.msharp.statement;

import com.example.msharp.expression.ExpressionHelper;
import com.example.msharp.Factor;
import com.example.msharp.Parser;

import java.util.ArrayList;
import java.util.Map;


/**
 * The IFStatement class represents an IF then ELSE block. This class inherits from Statement which allows it to implement an execute method.
 */
public class IfStatement extends Statement {
    public static final String TAG = "IFStatement";

    public Factor LHS;
    public String operator;
    public Factor RHS;
    public ArrayList<Statement> ifCodeBody;
    public ArrayList<Statement> elseCodeBody;
    public Integer startIndex;
    public Map<String, Integer> intMap;
    public Map<String, Double> doubleMap;
    public Map<String, String> stringMap;
    public Map<String, String> boolMap;
    public Parser parser;

    public IfStatement(Factor LHS, String operator, Factor RHS, ArrayList<Statement> ifCodeBody, ArrayList<Statement> elseCodeBody, Integer startIndex, Map<String, Integer> intMap, Map<String, Double> doubleMap, Map<String, String> stringMap, Map<String, String> boolMap, Parser parser) {
        this.LHS = LHS;
        this.operator = operator;
        this.RHS = RHS;
        this.ifCodeBody = ifCodeBody;
        this.elseCodeBody = elseCodeBody;
        this.startIndex = startIndex;
        this.intMap = intMap;
        this.doubleMap = doubleMap;
        this.stringMap = stringMap;
        this.boolMap = boolMap;
        this.parser = parser;
    }

    /**
     * This method, when invoked, will evaluated the condition in the if statement. It does so by initially evaluating the LHS factor of the expression. This result is used to determine the type. Once the type is determined, the RHS is also evaluated, and the result of the expression is determined.
     */
    @Override
    public void execute() {

        boolean isInt = false;
        boolean isDouble = false;
        boolean result = false;

        //Evaluate the LHS of the expression
        int LHSType = ExpressionHelper.executeAndGetType(TAG, LHS);

        //Determine whether the expression is an int or double
        switch (LHSType) {
            case ExpressionHelper.intLit:
                isInt = true;
                break;
            case ExpressionHelper.doubleLit:
                isDouble = true;
                break;
        }


        if (isInt) //the expression is of type INT
            result = ExpressionHelper.evaluateIntExpression(LHS, RHS, operator); //helper method to evaluate an integer expression
        else //the expression is of type BOOL
            result = ExpressionHelper.evaluateBoolExpression(LHS, RHS, operator); //helper method to evaluate an boolean expression

        //TODO: will need to consider if the expression is
        //      - type double
        //      - both type int and double

        if (result) {
            for (Statement s : ifCodeBody)
                s.execute();
        } else {
            for (Statement s : elseCodeBody)
                s.execute();
        }

    }

}
