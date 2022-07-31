package com.example.msharp22.decoration;

import com.example.msharp.Logging;
import com.example.msharp22.syntax.BinaryExpression;
import com.example.msharp22.syntax.Expression;
import com.example.msharp22.syntax.LiteralExpression;
import com.example.msharp22.syntax.TokenKind;
import com.example.msharp22.syntax.UnaryExpression;

import java.lang.reflect.Type;
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
    public DecoratedExpression decorateExpression(Expression expression) {
        switch (expression.kind()) {
            case Literal:
                return DecorateLiteralExpression((LiteralExpression) expression);
            case UnaryExpression:
                return DecorateUnaryExpression((UnaryExpression) expression);
            case BinaryExpression:
                return DecorateBinaryExpression((BinaryExpression) expression);
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
    private DecoratedExpression DecorateLiteralExpression(LiteralExpression expression) {
        Object value = expression.value == null ? 0 : expression.value;

        return new DecoratedLiteralExpression(value);

    }

    /**
     * This method is responsible for decorating a unary expression. It calls {@link Decorate#decorateExpression(Expression)} on the {@link UnaryExpression#operand} field of the UnaryExpression object. It also makes a call to {@link Decorate#decorateUnaryOperator} on the {@link UnaryExpression#operator} field of the passed in UnaryExpression object
     *
     * @param expression the UnaryExpression to be decorated
     * @return an object of type {@link DecoratedUnaryExpression} which is the decorated unary expression
     * @see Decorate#decorateExpression(Expression)
     * @see Decorate#decorateUnaryOperator(TokenKind, Type)
     */
    private DecoratedExpression DecorateUnaryExpression(UnaryExpression expression) {
        DecoratedExpression decoratedOperand = decorateExpression(expression.operand);
        DecoratedUnaryOperator decoratedOperator = decorateUnaryOperator(expression.operator.kind, decoratedOperand.type);

        if (decoratedOperator == null) {
            diagnostics.add(String.format("Unary operator %s is not defined for type %s", expression.operator.text, decoratedOperand.type));
            return decoratedOperand;
        }
        return new DecoratedUnaryExpression(decoratedOperator, decoratedOperand);
    }

    /**
     * This method is responsible for decorating a binary expression. It calls {@link Decorate#decorateExpression(Expression)} on the {@link BinaryExpression#left} and {@link BinaryExpression#right} fields of the BinaryExpression object. It also makes a call to {@link Decorate#decorateBinaryOperator(TokenKind, Type, Type)} on the {@link BinaryExpression#operator} field of the passed in BinaryExpression object
     *
     * @param expression the BinaryExpression to be decorated
     * @return an object of type {@link DecoratedBinaryExpression} which is the decorated binary expression
     * @see Decorate#decorateExpression(Expression)
     * @see Decorate#decorateBinaryOperator(TokenKind, Type, Type)
     */
    private DecoratedExpression DecorateBinaryExpression(BinaryExpression expression) {
        DecoratedExpression decoratedLeft = decorateExpression(expression.left);
        DecoratedExpression decoratedRight = decorateExpression(expression.right);
        DecoratedBinaryOperator decoratedOperator = decorateBinaryOperator(expression.operator.kind, decoratedLeft.type, decoratedRight.type);

        if (decoratedOperator == null) {
            diagnostics.add(String.format("Binary operator %s is not defined for types %s and %s", expression.operator.text, decoratedLeft.type, decoratedRight.type));
            return decoratedLeft;
        }
        return new DecoratedBinaryExpression(decoratedLeft, decoratedOperator, decoratedRight);
    }

    /**
     * This method decorates a unary operator. There are only two tokens to consider when decorating a unary expression, that is, the PlusToken and MinusToken
     *
     * @param kind        the kind of the operator token
     * @param operandType the class type of the operand. This object is used for type checking
     * @return an object of type {@link DecoratedUnaryOperator} which represents the decorated unary operator
     */
    private DecoratedUnaryOperator decorateUnaryOperator(TokenKind kind, Type operandType) {
        if (operandType != Double.class && operandType != Integer.class)
            return null;

        switch (kind) {
            case PlusToken:
                return DecoratedUnaryOperator.Identity;
            case MinusToken:
                return DecoratedUnaryOperator.Negation;
            default:
                Logging.error(TAG, new Exception("Unexpected unary operator " + kind));
                return null;
        }
    }

    /**
     * his method decorates a unary operator. There are only four tokens to consider when decorating a unary expression, that is, the PlusToken, MinusToken, MultToken and DivToken
     *
     * @param kind      the kind of the operator token
     * @param leftType  the class type of the left operand of the expression. This object is used for type checking
     * @param rightType the class type of the right operand of the expression. This object is used for type checking
     * @return an object of type {@link DecoratedBinaryOperator} which represents the decorated binary operator
     */
    private DecoratedBinaryOperator decorateBinaryOperator(TokenKind kind, Type leftType, Type rightType) {
        if ((leftType != Double.class && leftType != Integer.class) || (rightType != Double.class && rightType != Integer.class))
            return null;
        switch (kind) {
            case PlusToken:
                return DecoratedBinaryOperator.Add;
            case MinusToken:
                return DecoratedBinaryOperator.Sub;
            case MultToken:
                return DecoratedBinaryOperator.Mult;
            case DivToken:
                return DecoratedBinaryOperator.Div;
            default:
                Logging.error(TAG, new Exception("Unexpected binary operator " + kind));
                return null;
        }
    }
}


