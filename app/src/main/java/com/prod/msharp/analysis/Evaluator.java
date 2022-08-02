package com.prod.msharp.analysis;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.msharp.Logging;
import com.prod.msharp.analysis.decoration.Decorate;
import com.prod.msharp.analysis.decoration.DecoratedAssignmentExpression;
import com.prod.msharp.analysis.decoration.DecoratedBinaryExpression;
import com.prod.msharp.analysis.decoration.DecoratedExpression;
import com.prod.msharp.analysis.decoration.DecoratedLiteralExpression;
import com.prod.msharp.analysis.decoration.DecoratedUnaryExpression;
import com.prod.msharp.analysis.decoration.DecoratedVariableExpression;
import com.prod.msharp.analysis.syntax.SyntaxTree;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class will evaluate the result of an expression
 */
public class Evaluator {
    public static final String TAG = "Evaluator";

    public DecoratedExpression root;
    public Map<VariableSymbol, Object> variables;

    public Evaluator(DecoratedExpression root, Map<VariableSymbol, Object> variables) {
        this.root = root;
        this.variables = variables;
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

        if (node instanceof DecoratedVariableExpression)
            return variables.get(((DecoratedVariableExpression) node).variableSymbol);

        if (node instanceof DecoratedAssignmentExpression) {
            DecoratedAssignmentExpression a = (DecoratedAssignmentExpression) node;
            var value = evaluateExpression(a.expression);
            variables.put(a.variableSymbol, value);
            return value;
        }

        //Unary Expression: -4
        if (node instanceof DecoratedUnaryExpression) {
            DecoratedUnaryExpression due = (DecoratedUnaryExpression) node;
            Object operandResult = null;
            double doubleOperandResult;

            operandResult = evaluateExpression(due.operand);

            switch (((DecoratedUnaryExpression) node).op.kind) {
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
                    Logging.error(TAG, new Exception("Unexpected unary operator: " + ((DecoratedUnaryExpression) node).op));
                    break;
            }

        }

        //Binary Expressions: 2+4
        if (node instanceof DecoratedBinaryExpression) {
            DecoratedBinaryExpression b = (DecoratedBinaryExpression) node;
            Object left = evaluateExpression(b.left);
            Object right = evaluateExpression(b.right);

            int round = 10000; //rounding off factor

            switch (b.op.kind) {
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
                case Equals:
                    return left.equals(right);
                case NotEquals:
                    return !left.equals(right);
                default:
                    Logging.error(TAG, new Exception("Unexpected binary operator: " + b.op));

            }
        }

        Logging.error(TAG, new Exception("Unexpected node: " + node.kind));
        return 0;
    }


}
