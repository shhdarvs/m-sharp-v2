package com.prod.msharp.analysis.decoration;

import com.prod.msharp.analysis.VariableSymbol;

import java.lang.reflect.Type;
import java.util.List;

public class DecoratedAssignmentExpression extends DecoratedExpression{
    public VariableSymbol variableSymbol;
    public DecoratedExpression expression;

    public DecoratedAssignmentExpression(VariableSymbol variableSymbol, DecoratedExpression expression) {
        super(DecoratedNodeKind.AssignmentExpression, expression.type);
        this.variableSymbol = variableSymbol;
        this.expression = expression;
    }
}
