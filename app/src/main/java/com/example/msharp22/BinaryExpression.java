package com.example.msharp22;

import java.util.Arrays;
import java.util.List;

public class BinaryExpression extends Expression {
    public static final String TAG = "BinaryExpression";

    public Expression left;
    Token operator;
    public Expression right;

    public BinaryExpression(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public TokenKind kind() {
        return TokenKind.BinaryExpression;
    }

    @Override
    public List<AST> getChildren() {
        return Arrays.asList(left, operator, right);
    }
}
