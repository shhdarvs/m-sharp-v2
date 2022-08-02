package com.prod.msharp.analysis.syntax;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AssignmentExpression extends Expression {
    public Token identifierToken;
    Token arrowToken;
    public Expression expression;

    public AssignmentExpression(Token identifierToken, Token arrowToken, Expression expression) {
        this.identifierToken = identifierToken;
        this.arrowToken = arrowToken;
        this.expression = expression;
    }

    @Override
    public TokenKind kind() {
        return TokenKind.AssignmentExpression;
    }

    @Override
    public List<AST> getChildren() {
        return Arrays.asList(identifierToken, arrowToken, expression);
    }
}
