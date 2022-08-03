package com.prod.msharp.analysis;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.msharp.Logging;
import com.prod.msharp.analysis.decoration.Decorate;
import com.prod.msharp.analysis.decoration.DecoratedAssignmentExpression;
import com.prod.msharp.analysis.decoration.DecoratedBinaryExpression;
import com.prod.msharp.analysis.decoration.DecoratedExpression;
import com.prod.msharp.analysis.decoration.DecoratedLiteralExpression;
import com.prod.msharp.analysis.decoration.DecoratedNodeKind;
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
        switch (node.kind) {
            case LiteralExpression:
                return evaluateLiteralExpression((DecoratedLiteralExpression) node);
            case VariableExpression:
                return evaluateVariableExpression((DecoratedVariableExpression) node);
            case AssignmentExpression:
                return evaluateAssignmentExpression((DecoratedAssignmentExpression) node);
            case UnaryExpression:
                return evaluateUnaryExpression((DecoratedUnaryExpression) node);
            case BinaryExpression:
                return evaluateBinaryExpression((DecoratedBinaryExpression) node);
            default:
                return null;
        }
    }

    private Object evaluateLiteralExpression(DecoratedLiteralExpression node) {
        return node.value;
    }

    private Object evaluateVariableExpression(DecoratedVariableExpression node) {
        return variables.get(node.variableSymbol);
    }

    private Object evaluateAssignmentExpression(DecoratedAssignmentExpression node) {
        DecoratedAssignmentExpression a = node;
        var value = evaluateExpression(a.expression);
        variables.put(a.variableSymbol, value);
        return value;
    }

    private Object evaluateUnaryExpression(DecoratedUnaryExpression node) {
        Object result = null;

        result = evaluateExpression(node.operand);

        switch (node.op.kind) {
            case Identity:
                return result;
            case Negation:
                if (result.getClass() == Integer.class) {
                    return -(int) result;
                }
                return -(double) result;
            case LogicalNegation:
                return !(boolean) result;
            default:
                Logging.error(TAG, new Exception("Unexpected unary operator: " + node.op));
                break;
        }

        return result;
    }

    private Object evaluateBinaryExpression(DecoratedBinaryExpression node) {
        Object left = evaluateExpression(node.left);
        Object right = evaluateExpression(node.right);

        int round = 10000; //rounding off factor

        if (left != null && right != null) {
            switch (node.op.kind) {
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
                    Logging.error(TAG, new Exception("Unexpected binary operator: " + node.op));
            }
        }

        Logging.error(TAG, new Exception("Unexpected node: " + node.kind));
        return 0;
    }
    
}
