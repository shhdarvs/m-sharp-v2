package com.prod.msharp.analysis.decoration;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.prod.msharp.analysis.syntax.TokenKind;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DecoratedBinaryOperator {
    TokenKind tokenKind;
    public DecoratedBinaryOperatorKind kind;
    List<Type> left;
    List<Type> right;
    List<Type> returnType;

    private static final DecoratedBinaryOperator[] operators = {
            new DecoratedBinaryOperator(TokenKind.PlusToken, DecoratedBinaryOperatorKind.Add, Integer.class, Double.class),
            new DecoratedBinaryOperator(TokenKind.MinusToken, DecoratedBinaryOperatorKind.Sub, Integer.class, Double.class),
            new DecoratedBinaryOperator(TokenKind.MultToken, DecoratedBinaryOperatorKind.Mult, Integer.class, Double.class),
            new DecoratedBinaryOperator(TokenKind.DivToken, DecoratedBinaryOperatorKind.Div, Integer.class, Double.class),

            new DecoratedBinaryOperator(TokenKind.EqualsToken, DecoratedBinaryOperatorKind.Equals, Arrays.asList(Integer.class, Double.class), Collections.singletonList(Boolean.class)),
            new DecoratedBinaryOperator(TokenKind.NotEqualsToken, DecoratedBinaryOperatorKind.NotEquals, Arrays.asList(Integer.class, Double.class), Collections.singletonList(Boolean.class)),

            new DecoratedBinaryOperator(TokenKind.AndToken, DecoratedBinaryOperatorKind.LogicalAnd, Boolean.class),
            new DecoratedBinaryOperator(TokenKind.OrToken, DecoratedBinaryOperatorKind.LogicalOr, Boolean.class),
            new DecoratedBinaryOperator(TokenKind.EqualsToken, DecoratedBinaryOperatorKind.Equals, Boolean.class),
            new DecoratedBinaryOperator(TokenKind.NotEqualsToken, DecoratedBinaryOperatorKind.NotEquals, Boolean.class),

    };

    private DecoratedBinaryOperator(TokenKind tokenKind, DecoratedBinaryOperatorKind kind, Type... type) {
        this.tokenKind = tokenKind;
        this.kind = kind;
        this.left = Arrays.asList(type);
        this.right = this.left;
        this.returnType = this.left;
    }

    public DecoratedBinaryOperator(TokenKind tokenKind, DecoratedBinaryOperatorKind kind, List<Type> left, List<Type> returnType) {
        this.tokenKind = tokenKind;
        this.kind = kind;
        this.left = left;
        this.right = this.left;
        this.returnType = returnType;
    }

    public DecoratedBinaryOperator(TokenKind tokenKind, DecoratedBinaryOperatorKind kind, List<Type> left, List<Type> right, List<Type> returnType) {
        this.tokenKind = tokenKind;
        this.kind = kind;
        this.left = left;
        this.right = right;
        this.returnType = returnType;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public static DecoratedBinaryOperator decorate(TokenKind tokenKind, Type leftType, Type rightType) {
        for (DecoratedBinaryOperator duo : operators) {
            if (duo.tokenKind == tokenKind &&
                    duo.left.stream().anyMatch(b -> b.getTypeName().equals(leftType.getTypeName())) &&
                    duo.right.stream().anyMatch(b -> b.getTypeName().equals(rightType.getTypeName())))
                return duo;
        }

        return null;
    }

}
