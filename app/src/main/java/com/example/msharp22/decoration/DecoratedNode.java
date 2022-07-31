package com.example.msharp22.decoration;

abstract class DecoratedNode {
    public DecoratedNodeKind kind;

    public DecoratedNode(DecoratedNodeKind kind) {
        this.kind = kind;
    }
}
