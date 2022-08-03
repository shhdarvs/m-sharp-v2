package com.prod.msharp.analysis.syntax;

public class LiteralExpression extends Expression {

    public Token literal;
    public Object value;

    public LiteralExpression(Token literal) {
        this.literal = literal;
    }

    public LiteralExpression(Token literal, Object value) {
        this.literal = literal;
        this.value = value;
    }

    @Override
    public TokenKind kind() {
        return TokenKind.LiteralExpression;
    }

//    @Override
//    public List<AST> getChildren() {
//        return Collections.singletonList(literal);
//    }
}
