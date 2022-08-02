package com.prod.msharp.analysis.decoration;

abstract class DecoratedNode {
    public DecoratedNodeKind kind;

    public DecoratedNode(DecoratedNodeKind kind) {
        this.kind = kind;
    }
}
