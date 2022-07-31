package com.example.msharp22;

import com.example.msharp.Logging;
import com.example.msharp22.decoration.DecoratedBinaryExpression;
import com.example.msharp22.decoration.DecoratedExpression;
import com.example.msharp22.decoration.DecoratedLiteralExpression;
import com.example.msharp22.decoration.DecoratedUnaryExpression;


/**
 * This class will evaluate the result of an expression
 */
public class Evaluator {
    public static final String TAG = "Evaluator";

    public DecoratedExpression root;

    public Evaluator(DecoratedExpression root) {
        this.root = root;
    }

    /**
     * This method returns the result of evaluateExpression(Expression root)
     *
     * @return the result of evaluateExpression(Expression root)
     */
    public Object evaluate() {
        return evaluateExpression(root);
    }

    /**
     * This method evaluates an expression starting at the root. It determines whether the expression is a number expression or binary expression, and then evaluates accordingly
     *
     * @param node the root of the expression
     * @return the result of the expression (double)
     */
    private Object evaluateExpression(DecoratedExpression node) {
        //Current time of coding we only have two expressions:
        // 1. BinaryExpression
        // 2. NumberExpression (Either containing a int literal or double literal
        //3. Literals: 2, 5.6, true
        if (node instanceof DecoratedLiteralExpression)
            return ((DecoratedLiteralExpression) node).value;

        //Unary Expression: -4
        if (node instanceof DecoratedUnaryExpression) {
            DecoratedUnaryExpression due = (DecoratedUnaryExpression) node;
            double operandResult = 0;

            if (node.type == Integer.class) {
                if (due.operand instanceof DecoratedUnaryExpression)
                    operandResult = (double) evaluateExpression(due.operand);
                else
                    operandResult = (int) evaluateExpression(due.operand);
            } else
                operandResult = (double) evaluateExpression(due.operand);

            switch (((DecoratedUnaryExpression) node).opKind) {
                case Identity:
                    return operandResult;
                case Negation:
                    return -operandResult;
                default:
                    Logging.error(TAG, new Exception("Unexpected unary operator: " + ((DecoratedUnaryExpression) node).opKind));
                    break;
            }

        }

        //Binary Expressions: 2+4
        if (node instanceof DecoratedBinaryExpression) {
            DecoratedBinaryExpression b = (DecoratedBinaryExpression) node;
            double left = (double) evaluateExpression(b.left);
            double right = (double) evaluateExpression(b.right);

            int round = 10000;

            switch (b.opKind) {
                case Add:
                    return (double) Math.round((left + right) * round) / round;
                case Sub:
                    return (double) Math.round((left - right) * round) / round;
                case Mult:
                    return (double) Math.round((left * right) * round) / round;
                case Div:
                    return (double) Math.round((left / right) * round) / round;
                default:
                    Logging.error(TAG, new Exception("Unexpected binary operator: " + b.opKind));

            }
        }

        Logging.error(TAG, new Exception("Unexpected node: " + node.kind));
        return 0;
    }


}
