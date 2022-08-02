package com.prod.msharp.analysis.decoration;

import com.prod.msharp.analysis.VariableSymbol;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class DecoratedVariableExpression extends DecoratedExpression {
    public VariableSymbol variableSymbol;

    public DecoratedVariableExpression(VariableSymbol variableSymbol) {
        super(DecoratedNodeKind.VariableExpression, Collections.singletonList(variableSymbol.type));
        this.variableSymbol = variableSymbol;
    }
}
