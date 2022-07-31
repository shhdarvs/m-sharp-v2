package com.example.msharp22.syntax;

import java.util.Collections;
import java.util.List;

public class IntegerLiteral extends Expression {
    public static final String TAG = "IntegerLiteral";

    public Token intToken;

    public IntegerLiteral(Token intToken) {
        this.intToken = intToken;
    }

    @Override
    public TokenKind kind() {
        return TokenKind.IntegerToken;
    }

    @Override
    public List<AST> getChildren() {
        return Collections.singletonList(intToken);
    }
}
