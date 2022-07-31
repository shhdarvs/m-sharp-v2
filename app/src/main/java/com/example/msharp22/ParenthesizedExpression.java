package com.example.msharp22;

import java.util.Arrays;
import java.util.List;

public class ParenthesizedExpression extends Expression {
    public Token openParenthesis;
    public Expression expression;
    public Token closedParenthesis;

    public ParenthesizedExpression(Token openParenthesis, Expression expression, Token closedParenthesis) {
        this.openParenthesis = openParenthesis;
        this.expression = expression;
        this.closedParenthesis = closedParenthesis;
    }

    @Override
    public TokenKind kind() {
        return TokenKind.ParenthesizedExpression;
    }

    @Override
    public List<AST> getChildren() {
        return Arrays.asList(openParenthesis, expression, closedParenthesis);
    }
}
