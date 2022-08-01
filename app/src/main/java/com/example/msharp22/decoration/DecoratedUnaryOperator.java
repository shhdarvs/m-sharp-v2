package com.example.msharp22.decoration;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.msharp22.syntax.TokenKind;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class DecoratedUnaryOperator {
    TokenKind tokenKind;
    public DecoratedUnaryOperatorKind kind;
    List<Type> operandType;
    List<Type> returnType;

    private static final DecoratedUnaryOperator[] operators = {
            new DecoratedUnaryOperator(TokenKind.NotToken, DecoratedUnaryOperatorKind.LogicalNegation, boolean.class),
            new DecoratedUnaryOperator(TokenKind.PlusToken, DecoratedUnaryOperatorKind.Identity, Integer.class, Double.class),
            new DecoratedUnaryOperator(TokenKind.MinusToken, DecoratedUnaryOperatorKind.Negation, Integer.class, Double.class)
    };

    private DecoratedUnaryOperator(TokenKind tokenKind, DecoratedUnaryOperatorKind kind, Type... operandType) {
        this.tokenKind = tokenKind;
        this.kind = kind;
        this.operandType = Arrays.asList(operandType);
        this.returnType = this.operandType;
    }

    private DecoratedUnaryOperator(TokenKind tokenKind, DecoratedUnaryOperatorKind kind, List<Type> operandType, List<Type> returnType) {
        this.tokenKind = tokenKind;
        this.kind = kind;
        this.operandType = operandType;
        this.returnType = returnType;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static DecoratedUnaryOperator decorate(TokenKind tokenKind, Type operandType) {
        for (DecoratedUnaryOperator duo : operators) {
            if (duo.tokenKind == tokenKind &&
                    duo.operandType.stream().anyMatch(b -> b.getClass() == operandType.getClass()))
                return duo;
        }

        return null;
    }

}
