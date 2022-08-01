package com.example.msharp22;

import com.example.msharp.Logging;
import com.example.msharp22.decoration.DecoratedBinaryExpression;
import com.example.msharp22.decoration.DecoratedExpression;
import com.example.msharp22.decoration.DecoratedLiteralExpression;
import com.example.msharp22.decoration.DecoratedUnaryExpression;

import static com.example.msharp22.decoration.DecoratedBinaryOperator.Sub;


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
            Object operandResult = null;
            double doubleOperandResult;

            operandResult = evaluateExpression(due.operand);

            switch (((DecoratedUnaryExpression) node).opKind) {
                case Identity:
                    return operandResult;
                case Negation:
                    if (operandResult.getClass() == Integer.class) {
                        doubleOperandResult = (int) operandResult;
                        return -doubleOperandResult;
                    }
                    return -(double) operandResult;
                case LogicalNegation:
                    return !(boolean) operandResult;
                default:
                    Logging.error(TAG, new Exception("Unexpected unary operator: " + ((DecoratedUnaryExpression) node).opKind));
                    break;
            }

        }

        //Binary Expressions: 2+4
        if (node instanceof DecoratedBinaryExpression) {
            DecoratedBinaryExpression b = (DecoratedBinaryExpression) node;
            Object left = evaluateExpression(b.left);
            Object right = evaluateExpression(b.right);

            int round = 10000; //rounding off factor

            switch (b.opKind) {
                case Add:
                    if (left.getClass() == Integer.class && right.getClass() == Integer.class)
                        return (double) Math.round(((int) left + (int) right) * round) / round;
                    else if (left.getClass() == Integer.class)
                        return (double) Math.round(((int) left + (double) right) * round) / round;
                    else if (right.getClass() == Integer.class)
                        return (double) Math.round(((double) left + (int) right) * round) / round;
                    else
                        return (double) Math.round(((double) left + (double) right) * round) / round;
                case Sub:
                    if (left.getClass() == Integer.class && right.getClass() == Integer.class)
                        return (double) Math.round(((int) left - (int) right) * round) / round;
                    else if (left.getClass() == Integer.class)
                        return (double) Math.round(((int) left - (double) right) * round) / round;
                    else if (right.getClass() == Integer.class)
                        return (double) Math.round(((double) left - (int) right) * round) / round;
                    else
                        return (double) Math.round(((double) left - (double) right) * round) / round;
                case Mult:
                    if (left.getClass() == Integer.class && right.getClass() == Integer.class)
                        return (double) Math.round(((int) left * (int) right) * round) / round;
                    else if (left.getClass() == Integer.class)
                        return (double) Math.round(((int) left * (double) right) * round) / round;
                    else if (right.getClass() == Integer.class)
                        return (double) Math.round(((double) left * (int) right) * round) / round;
                    else
                        return (double) Math.round(((double) left * (double) right) * round) / round;
                case Div:
                    if (left.getClass() == Integer.class && right.getClass() == Integer.class)
                        return (double) Math.round(((int) left / (int) right) * round) / round;
                    else if (left.getClass() == Integer.class)
                        return (double) Math.round(((int) left / (double) right) * round) / round;
                    else if (right.getClass() == Integer.class)
                        return (double) Math.round(((double) left / (int) right) * round) / round;
                    else
                        return (double) Math.round(((double) left / (double) right) * round) / round;
                case LogicalAnd:
                    return (boolean) left && (boolean) right;
                case LogicalOr:
                    return (boolean) left || (boolean) right;
                default:
                    Logging.error(TAG, new Exception("Unexpected binary operator: " + b.opKind));

            }
        }

        Logging.error(TAG, new Exception("Unexpected node: " + node.kind));
        return 0;
    }


}
