package com.example.msharp22;

import com.example.msharp.Logging;

/**
 * This class will evaluate the result of an expression
 */
public class Evaluator {
    public static final String TAG = "Evaluator";

    public Expression root;

    public Evaluator(Expression root) {
        this.root = root;
    }

    /**
     * This method returns the result of evaluateExpression(Expression root)
     *
     * @return the result of evaluateExpression(Expression root)
     */
    public double evaluate() {
        return evaluateExpression(root);
    }

    /**
     * This method evaluates an expression starting at the root. It determines whether the expression is a number expression or binary expression, and then evaluates accordingly
     *
     * @param node the root of the expression
     * @return the result of the expression (double)
     */
    private double evaluateExpression(Expression node) {
        //Current time of coding we only have two expressions:
        // 1. BinaryExpression
        // 2. NumberExpression (Either containing a int literal or double literal

        if (node instanceof LiteralExpression) {
            LiteralExpression n = (LiteralExpression) node;

            if (n.literal.kind == TokenKind.IntegerToken) {
                return (int) n.literal.value;
            } else
                return (double) n.literal.value;

        }

        if (node instanceof BinaryExpression) {
            BinaryExpression b = (BinaryExpression) node;
            double left = evaluateExpression(b.left);
            double right = evaluateExpression(b.right);

            int round = 10000;

            switch (b.operator.kind) {
                case PlusToken:
                    return (double) Math.round((left + right) * round) / round;
                case MinusToken:
                    return (double) Math.round((left - right) * round) / round;
                case MultToken:
                    return (double) Math.round((left * right) * round) / round;
                case DivToken:
                    return (double) Math.round((left / right) * round) / round;
                default:
                    Logging.error(TAG, new Exception("Unexpected binary operator: " + b.operator.kind));

            }
        }

        if (node instanceof ParenthesizedExpression)
            return evaluateExpression(((ParenthesizedExpression) node).expression);

        Logging.error(TAG, new Exception("Unexpected node: " + node.kind()));
        return 0;
    }


}
