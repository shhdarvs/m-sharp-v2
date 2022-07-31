package com.example.msharp22.decoration;

public class DecoratedUnaryExpression extends DecoratedExpression {
    public DecoratedUnaryOperator opKind;
    public DecoratedExpression operand;

    public DecoratedUnaryExpression(DecoratedUnaryOperator opKind, DecoratedExpression operand) {
        super(DecoratedNodeKind.UnaryExpression, operand.type);
        this.opKind = opKind;
        this.operand = operand;
    }
}
