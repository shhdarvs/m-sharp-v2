package com.example.msharp22.decoration;

import java.lang.reflect.Type;

public abstract class DecoratedExpression extends DecoratedNode {
    public Type type;

    public DecoratedExpression(DecoratedNodeKind kind, Type type) {
        super(kind);
        this.type = type;
    }

}
