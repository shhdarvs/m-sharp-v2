package com.prod.msharp.analysis.syntax;

import java.util.Collections;
import java.util.List;

public class DoubleLiteral extends Expression {
    public static final String TAG = "DoubleLiteral";

    public Token doubleToken;

    public DoubleLiteral(Token doubleToken) {
        this.doubleToken = doubleToken;
    }

    @Override
    public TokenKind kind() {
        return TokenKind.DoubleToken;
    }

    @Override
    public List<AST> getChildren() {
        return Collections.singletonList(doubleToken);
    }
}
