package com.example.msharp22.decoration;

public class DecoratedBinaryExpression extends DecoratedExpression {
    public DecoratedExpression left;
    public DecoratedBinaryOperator opKind;
    public DecoratedExpression right;

    public DecoratedBinaryExpression(DecoratedExpression left, DecoratedBinaryOperator opKind, DecoratedExpression right) {
        super(DecoratedNodeKind.BinaryExpression, left.type);
        this.left = left;
        this.opKind = opKind;
        this.right = right;
    }
}
