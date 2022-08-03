package com.prod.msharp.analysis.syntax;

import java.util.Arrays;
import java.util.List;

public class UnaryExpression extends Expression {
    private static final String TAG = "UnaryExpression";

    public Token operator;
    public Expression operand;

    public UnaryExpression(Token operator, Expression operand) {
        this.operator = operator;
        this.operand = operand;
    }

    @Override
    public TokenKind kind() {
        return TokenKind.UnaryExpression;
    }

//    @Override
//    public List<AST> getChildren() {
//        return Arrays.asList(operator, operand);
//    }
}
