package com.prod.msharp.analysis.syntax;


import androidx.annotation.NonNull;

import com.prod.msharp.analysis.TextSpan;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Token in the source program. The different kinds of token are listed as enums in {@link TokenKind}. Each token has a position in the source program, and string value, an object value, and a {@link TextSpan} object which represents the span of its text.
 */
public class Token extends AST {
    public TokenKind kind;
    public int pos;
    public final String text;
    public Object value;
    public TextSpan textSpan;

    public Token(TokenKind kind, int pos, String text, Object value) {
        this.kind = kind;
        this.pos = pos;
        this.text = text;
        this.value = value;
        this.textSpan = new TextSpan(pos, text.length());
    }

    @NonNull
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
