package com.example.msharp.statement;

import com.example.msharp.expression.ExpressionHelper;
import com.example.msharp.Factor;
import com.example.msharp.Logging;
import com.example.msharp.Parser;

import java.util.ArrayList;
import java.util.Map;

/* While statement */

/**
 * The WhileStatement class represents a WHILE statement/loop. When executed, the body of the statement is executed if thee expression is true.
 */
public class WhileStatement extends Statement {
    public static final String TAG = "WhileStatement";

    public Factor LHS;
    public String operator;
    public Factor RHS;
    public ArrayList<Statement> codeBody;
    public Integer startIndex;
    public Map<String, Integer> intMap;
    public Map<String, Double> doubleMap;
    public Map<String, String> stringMap;
    public Map<String, String> boolMap;
    public Parser parser;

    public WhileStatement(Factor LHS, String operator, Factor RHS, ArrayList<Statement> codeBody, Integer startIndex, Map<String, Integer> intMap, Map<String, Double> doubleMap, Map<String, String> stringMap, Map<String, String> boolMap, Parser parser) {
        this.LHS = LHS;
        this.operator = operator;
        this.RHS = RHS;
        this.codeBody = codeBody;
        this.startIndex = startIndex;
        this.intMap = intMap;
        this.doubleMap = doubleMap;
        this.stringMap = stringMap;
        this.boolMap = boolMap;
        this.parser = parser;
    }

    /**
     * This method, when invoked, will evaluated the condition in the while statement. It does so by initially evaluating the LHS factor of the expression. This result is used to determine the type. Once the type is determined, the RHS is also evaluated, and the result of the expression is determined.
     * If the result is true, the while loop will execute every statement in its body. Once done, the expression will be re-evaluated to determine if the loop should be executed once more.
     */
    public void execute() {

        //Flag variables to keep track of some information
        boolean isInt = false;
        boolean isDouble = false;
        boolean result = false;

        //Evaluate the LHS of the expression
        int LHSType = ExpressionHelper.executeAndGetType(TAG, LHS);

        ///Determine whether the expression is an int or double
        switch (LHSType) {
            case ExpressionHelper.intLit:
                isInt = true;
                break;
            case ExpressionHelper.doubleLit:
                isDouble = true;
                break;
        }

        if (isInt) {
            result = ExpressionHelper.evaluateIntExpression(LHS, RHS, operator);

            long start = System.currentTimeMillis();
            long end = start + 10000; // 60 seconds * 1000 ms/sec
            boolean running = true;

            while (System.currentTimeMillis() < end && running) //infinite loop recovery
            {
                while (result && System.currentTimeMillis() < end) {
                    for (Statement s : codeBody)
                        s.execute();


                    result = ExpressionHelper.evaluateIntExpression(LHS, RHS, operator);
                }
                running = false;

            }

            if (result)
                Logging.error(TAG, new Exception("Infinite loop detected."));

        } else {

            result = ExpressionHelper.evaluateBoolExpression(LHS, RHS, operator);

            long start = System.currentTimeMillis();
            long end = start + 10000; // 60 seconds * 1000 ms/sec
            boolean running = true;

            while (System.currentTimeMillis() < end && running) {
                while (result && System.currentTimeMillis() < end) {
                    for (Statement s : codeBody)
                        s.execute();

                    result = ExpressionHelper.evaluateBoolExpression(LHS, RHS, operator);
                }
                running = false;
            }

            if (result)
                Logging.error(TAG, new Exception("Infinite loop detected."));
        }

        //TODO: will need to consider if the expression is
        //      - type double
        //      - both type int and double
    }
}
