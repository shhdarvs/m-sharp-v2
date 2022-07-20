package com.example.msharp.statement;

import com.example.msharp.expression.Expression;
import com.example.msharp.expression.ExpressionHelper;
import com.example.msharp.Logging;

import java.util.Map;

/**
 * The AssignmentStatement class represents an assignment statement. The result of an expression is assigned to a variable. All variables declared in the program are stored in reference maps. Each map stores variables of a particular type.
 */
public class AssignmentStatement extends Statement {
    public static final String TAG = "AssignmentStatement";

    public String variable;
    public Expression expression;
    public Map<String, Integer> intMap;
    public Map<String, Double> doubleMap;
    public Map<String, String> stringMap;
    public Map<String, String> boolMap;

    public AssignmentStatement(String variable, Expression expression, Map<String, Integer> intMap, Map<String, Double> doubleMap, Map<String, String> stringMap, Map<String, String> boolMap) {
        this.variable = variable;
        this.expression = expression;
        this.intMap = intMap;
        this.doubleMap = doubleMap;
        this.stringMap = stringMap;
        this.boolMap = boolMap;
    }

    /**
     * This method removes all references of the variable associated to this assignment from all the reference maps
     */
    public void removeVariable() {
        intMap.remove(variable);
        boolMap.remove(variable);
        stringMap.remove(variable);
        doubleMap.remove(variable);
    }



    /*Assign a value to a variable. */

    /**
     * This method executes an assignment statement. An expression is evaluated and the type of its result is determined. This result is then assigned the variable associated with this assignment in the correct reference map.
     */
    @Override
    public void execute() {
        //The result of the expression is evaluated
        try {
            expression.execute();
        } catch (Exception e) {
            Logging.error(TAG, e);
        }

        int type = 0;
        try {
            type = expression.type();
        } catch (Exception e) {
            Logging.error(TAG, e);
        }

        //Dependent on the kind of result, the result is placed into the respective reference map
        switch (type) {
            case ExpressionHelper.addExp:
            case ExpressionHelper.multExp:
            case ExpressionHelper.intLit:
                removeVariable();
                intMap.put(variable, expression.resultInt());
                break;

            case ExpressionHelper.andExp:
            case ExpressionHelper.eqlExp:
            case ExpressionHelper.relExp:
            case ExpressionHelper.boolLit:
                removeVariable();
                boolMap.put(variable, expression.resultBool());
                break;

            case ExpressionHelper.stringLit:
                removeVariable();
                stringMap.put(variable, expression.resultString());
                break;

            case ExpressionHelper.doubleLit:
                removeVariable();
                //TODO: place double result into map
            default:
                Logging.error(TAG, new Exception("Attempting to access undeclared variable"));


        }

    }
}
