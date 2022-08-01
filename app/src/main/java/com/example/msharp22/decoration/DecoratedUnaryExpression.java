package com.example.msharp22.decoration;

public class DecoratedUnaryExpression extends DecoratedExpression {
    public DecoratedUnaryOperator op;
    public DecoratedExpression operand;

    public DecoratedUnaryExpression(DecoratedUnaryOperator op, DecoratedExpression operand) {
        super(DecoratedNodeKind.UnaryExpression, operand.type);
        this.op = op;
        this.operand = operand;
    }
}
