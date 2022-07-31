package com.example.msharp22.syntax;


import java.util.ArrayList;
import java.util.List;

public class Token extends AST {
    public TokenKind kind;
    public int pos;
    public final String text;
    public Object value;

    public Token(TokenKind kind, int pos, String text, Object value) {
        this.kind = kind;
        this.pos = pos;
        this.text = text;
        this.value = value;
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    public TokenKind kind() {
        return kind;
    }

    @Override
    public List<AST> getChildren() {
        return new ArrayList<>();
    }
}
