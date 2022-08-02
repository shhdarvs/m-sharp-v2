package com.prod.msharp.analysis.decoration;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.msharp.Logging;
import com.prod.msharp.analysis.syntax.BinaryExpression;
import com.prod.msharp.analysis.syntax.Expression;
import com.prod.msharp.analysis.syntax.LiteralExpression;
import com.prod.msharp.analysis.syntax.ParenthesizedExpression;
import com.prod.msharp.analysis.syntax.UnaryExpression;

import java.util.ArrayList;
import java.util.List;

public class Decorate {
    public static final String TAG = "Decorate";

    public List<String> diagnostics = new ArrayList<>();

    /**
     * This method decorates an expression depending on the type. The decoration for unary and binary expression performs some type checking
     *
     * @param expression the expression to be decorated
     * @return an object of type DecoratedExpression which represents the decorated expression
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public DecoratedExpression decorateExpression(Expression expression) {
        switch (expression.kind()) {
            case Literal:
                return decorateLiteralExpression((LiteralExpression) expression);
            case UnaryExpression:
                return decorateUnaryExpression((UnaryExpression) expression);
            case BinaryExpression:
                return decorateBinaryExpression((BinaryExpression) expression);
            case ParenthesizedExpression:
                return decorateExpression(((ParenthesizedExpression) expression).expression);
            default:
                Logging.error(TAG, new Exception("Unexpected syntax " + expression.kind()));
                return null;
        }

    }

    /**
     * This method is responsible for decorating a literal expression. It will convert the value of the literal field of the LiteralExpression object to either an int or double
     *
     * @param expression the LiteralExpression to be decorated
     * @return an object of type {@link DecoratedLiteralExpression} which is the decorated literal expression
     */
    private DecoratedExpression decorateLiteralExpression(LiteralExpression expression) {
        Object value = expression.value == null ? 0 : expression.value;

        return new DecoratedLiteralExpression(value);

    }

    /**
     * This method is responsible for decorating a unary expression. It calls {@link Decorate#decorateExpression(Expression)} on the {@link UnaryExpression#operand} field of the UnaryExpression object. It also makes a call to -- on the {@link UnaryExpression#operator} field of the passed in UnaryExpression object
     *
     * @param expression the UnaryExpression to be decorated
     * @return an object of type {@link DecoratedUnaryExpression} which is the decorated unary expression
     * @see Decorate#decorateExpression(Expression)
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    private DecoratedExpression decorateUnaryExpression(UnaryExpression expression) {
        DecoratedExpression decoratedOperand = decorateExpression(expression.operand);
        DecoratedUnaryOperator decoratedOperator = DecoratedUnaryOperator.decorate(expression.operator.kind, decoratedOperand.type.get(0));

        if (decoratedOperator == null) {
            diagnostics.add(String.format("Unary operator %s is not defined for type %s", expression.operator.text, decoratedOperand.type));
            return decoratedOperand;
        }
        return new DecoratedUnaryExpression(decoratedOperator, decoratedOperand);
    }

    /**
     * This method is responsible for decorating a binary expression. It calls {@link Decorate#decorateExpression(Expression)} on the {@link BinaryExpression#left} and {@link BinaryExpression#right} fields of the BinaryExpression object. It also makes a call to -- on the {@link BinaryExpression#operator} field of the passed in BinaryExpression object
     *
     * @param expression the BinaryExpression to be decorated
     * @return an object of type {@link DecoratedBinaryExpression} which is the decorated binary expression
     * @see Decorate#decorateExpression(Expression)
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    private DecoratedExpression decorateBinaryExpression(BinaryExpression expression) {
        DecoratedExpression decoratedLeft = decorateExpression(expression.left);
        DecoratedExpression decoratedRight = decorateExpression(expression.right);
        DecoratedBinaryOperator decoratedOperator = DecoratedBinaryOperator.decorate(expression.operator.kind, decoratedLeft.type.get(0), decoratedRight.type.get(0));

        if (decoratedOperator == null) {
            diagnostics.add(String.format("Binary operator %s is not defined for types %s and %s", expression.operator.text, decoratedLeft.type, decoratedRight.type));
            return decoratedLeft;
        }
        return new DecoratedBinaryExpression(decoratedLeft, decoratedOperator, decoratedRight);
    }


}


