package com.prod.msharp.analysis.decoration;

import java.util.Collections;

public class DecoratedLiteralExpression extends DecoratedExpression {
    public Object value;

    public DecoratedLiteralExpression(Object value) {
        super(DecoratedNodeKind.LiteralExpression, Collections.singletonList(value.getClass()));
        this.value = value;
    }
}
