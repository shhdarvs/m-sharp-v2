package com.example.msharp22.decoration;

public class DecoratedLiteralExpression extends DecoratedExpression {
    public Object value;

    public DecoratedLiteralExpression(Object value) {
        super(DecoratedNodeKind.LiteralExpression, value.getClass());
        this.value = value;
    }
}
