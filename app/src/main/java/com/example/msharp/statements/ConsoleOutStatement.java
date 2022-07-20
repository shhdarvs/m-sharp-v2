package com.example.msharp.statements;

import android.widget.TextView;

import com.example.msharp.Expression;
import com.example.msharp.ExpressionHelper;

import java.util.Map;

/**
 * The ConsoleOutStatement will print the result of any expression to the ConsoleActivity
 */
public class ConsoleOutStatement extends Statement {
    public static final String TAG = "ConsoleOutStatement";

    public Expression expression;
    Map<String, Integer> intMap;
    Map<String, Double> doubleMap;
    Map<String, String> stringMap;
    Map<String, String> boolMap;
    TextView console;

    public ConsoleOutStatement(Expression expression, Map<String, Integer> intMap, Map<String, Double> doubleMap, Map<String, String> stringMap, Map<String, String> boolMap, TextView console) {
        this.expression = expression;
        this.intMap = intMap;
        this.doubleMap = doubleMap;
        this.stringMap = stringMap;
        this.boolMap = boolMap;
        this.console = console;
    }


    @Override
    public void execute() {
        //Using helper class to execute and determine type of expression
        int type = ExpressionHelper.executeAndGetType(TAG, expression);
        switch (type) {
            case ExpressionHelper.addExp:
            case ExpressionHelper.multExp:
            case ExpressionHelper.intLit:
                console.append(expression.resultInt() + "\r\n");
                break;

            case ExpressionHelper.andExp:
            case ExpressionHelper.eqlExp:
            case ExpressionHelper.relExp:
            case ExpressionHelper.boolLit:
                console.append(expression.resultBool() + "\r\n");
                break;

            case ExpressionHelper.stringLit:
                String[] stringOut = expression.resultString().split("#", 2);
                console.append(stringOut[1] + "\r\n");
                break;

            //TODO: add case for appending double expression to console
        }

    }
}
