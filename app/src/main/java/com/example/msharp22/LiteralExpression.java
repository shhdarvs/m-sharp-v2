package com.example.msharp22;

import java.util.Collections;
import java.util.List;

public class LiteralExpression extends Expression {

    public Token literal;

    public LiteralExpression(Token literal) {
        this.literal = literal;
    }

    @Override
    public TokenKind kind() {
        return TokenKind.Literal;
    }

    @Override
    public List<AST> getChildren() {
        return Collections.singletonList(literal);
    }
}
