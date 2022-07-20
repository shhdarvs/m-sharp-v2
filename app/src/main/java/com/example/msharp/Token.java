package com.example.msharp;

public class Token {
    public byte kind;
    public String spelling;

    public Token(byte kind, String spelling) {
        this.kind = kind;
        this.spelling = spelling;
    }

    //Constants denoting different kinds of tokens
    public final static byte
        IDENTIFIER      = 0,
        INTLITERAL      = 1,
        DOUBLELITERAL   = 2,
        STRINGLITERAL   = 3,
        OPERATOR        = 4,
        BEGIN           = 5,    // begin
        END             = 6,    // end
        DO              = 7,    // do
        IF              = 8,    // if
        ELSE            = 9,    // else
        THEN            = 10,   // then
        WHILE           = 11,   // while
        SEMICOLON       = 12,   // ;
        COLON           = 13,   // :
        EQUALS          = 14,   // =
        LPAREN          = 15,   // (
        RPAREN          = 16,   // )
        EOT             = 17;   // end of text


}
