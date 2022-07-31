package com.example.msharp22.syntax;

import java.util.List;

public abstract class AST {
    public abstract TokenKind kind();
    public abstract List<AST> getChildren();
}