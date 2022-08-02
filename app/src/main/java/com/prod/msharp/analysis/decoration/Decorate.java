package com.prod.msharp.analysis.decoration;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.msharp.Logging;
import com.prod.msharp.analysis.DiagnosticSet;
import com.prod.msharp.analysis.syntax.AssignmentExpression;
import com.prod.msharp.analysis.syntax.BinaryExpression;
import com.prod.msharp.analysis.syntax.Expression;
import com.prod.msharp.analysis.syntax.LiteralExpression;
import com.prod.msharp.analysis.syntax.NameExpression;
import com.prod.msharp.analysis.syntax.ParenthesizedExpression;
import com.prod.msharp.analysis.syntax.UnaryExpression;

import java.util.ArrayList;
import java.util.List;

public class Decorate {
    public static final String TAG = "Decorate";

    public DiagnosticSet diagnostics = new DiagnosticSet();

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
            case NameExpression:
                return decorateNameExpression(((NameExpression) expression));
            case AssignmentExpression:
                return decorateAssignmentExpression(((AssignmentExpression) expression));
            case UnaryExpression:
                return decorateUnaryExpression((UnaryExpression) expression);
            case BinaryExpression:
                return decorateBinaryExpression((BinaryExpression) expression);
            case ParenthesizedExpression:
                return decorateParenthesizedExpression(((ParenthesizedExpression) expression));


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

    private DecoratedExpression decorateNameExpression(NameExpression expression) {
        return null;
    }

    private DecoratedExpression decorateAssignmentExpression(AssignmentExpression expression) {
        return null;
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
            diagnostics.reportUndefinedUnaryOperator(expression.operator.textSpan, expression.operator.text, decoratedOperand.type.get(0));
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
            diagnostics.reportUndefinedBinaryOperator(expression.operator.textSpan, expression.operator.text, decoratedLeft.type.get(0), decoratedRight.type.get(0));
            return decoratedLeft;
        }
        return new DecoratedBinaryExpression(decoratedLeft, decoratedOperator, decoratedRight);
    }

    /**
     * This method is responsible for decorating a parenthesized expression. It calls {@link Decorate#decorateExpression(Expression)} on the {@link ParenthesizedExpression} object which is passed in as a parameter.
     *
     * @param expression the parenthesized expression to be decorated
     * @return an object of type {@link DecoratedExpression} which represents the decorated parenthesized expression
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    private DecoratedExpression decorateParenthesizedExpression(ParenthesizedExpression expression) {
        return decorateExpression(expression.expression);
    }


}


