package com.prod.msharp.analysis.syntax;

import java.util.Collections;
import java.util.List;

public class LiteralExpression extends Expression {

    public Token literal;
    public Object value;

    public LiteralExpression(Token literal, Object value) {
        this.literal = literal;
        this.value = value;
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
