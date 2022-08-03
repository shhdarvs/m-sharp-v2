package com.prod.msharp.analysis.syntax;

import java.util.Collections;
import java.util.List;

public class NameExpression extends Expression {
    public Token identifierToken;

    public NameExpression(Token identifierToken) {
        this.identifierToken = identifierToken;
    }

    @Override
    public TokenKind kind() {
        return TokenKind.NameExpression;
    }

//    @Override
//    public List<AST> getChildren() {
//        return Collections.singletonList(identifierToken);
//    }
}
