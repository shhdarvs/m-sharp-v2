package com.prod.msharp.analysis.decoration;

import java.lang.reflect.Type;
import java.util.List;

public abstract class DecoratedExpression extends DecoratedNode {
    public List<Type> type;

    public DecoratedExpression(DecoratedNodeKind kind, List<Type> type) {
        super(kind);
        this.type = type;
    }

}
