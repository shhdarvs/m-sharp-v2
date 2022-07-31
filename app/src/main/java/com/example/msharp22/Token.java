package com.example.msharp22;

import com.example.msharp22.TokenKind;

import java.util.ArrayList;
import java.util.List;

public class Token extends AST {
    public TokenKind kind;
    public int pos;
    private String text;
    public Object value;

    public Token(TokenKind kind, int pos, String text, Object value) {
        this.kind = kind;
        this.pos = pos;
        this.text = text;
        this.value = value;
    }

    //Constants denoting different kinds of tokens
    public final static byte
            IDENTIFIER = 0,
            INTLITERAL = 1,
            DOUBLELITERAL = 2,
            STRINGLITERAL = 3,
            OPERATOR = 4,
            BEGIN = 5,    // begin
            END = 6,    // end
            DO = 7,    // do
            IF = 8,    // if
            ELSE = 9,    // else
            THEN = 10,   // then
            WHILE = 11,   // while
            SEMICOLON = 12,   // ;
            COLON = 13,   // :
            EQUALS = 14,   // =
            LPAREN = 15,   // (
            RPAREN = 16,   // )
            EOT = 17;   // end of text


    @Override
    public TokenKind kind() {
        return kind;
    }

    @Override
    public List<AST> getChildren() {
        return new ArrayList<>();
    }
}
