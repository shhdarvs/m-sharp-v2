package com.prod.msharp.analysis.decoration;

public class DecoratedBinaryExpression extends DecoratedExpression {
    public DecoratedExpression left;
    public DecoratedBinaryOperator op;
    public DecoratedExpression right;

    public DecoratedBinaryExpression(DecoratedExpression left, DecoratedBinaryOperator op, DecoratedExpression right) {
        super(DecoratedNodeKind.BinaryExpression, op.returnType);
        this.left = left;
        this.op = op;
        this.right = right;
    }
}