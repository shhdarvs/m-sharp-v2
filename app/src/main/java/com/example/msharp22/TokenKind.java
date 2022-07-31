package com.example.msharp22;

public enum TokenKind {

    //Tokens
    EOFToken,
    ERROR,
    WhitespaceToken,
    IntegerToken,
    DoubleToken,
    StringToken,
    BoolToken,
    PlusToken,
    MinusToken,
    MultToken,
    DivToken,
    OpenParenthesis,
    ClosedParenthesis,

    //Expressions
    BinaryExpression,
    Literal,
    ParenthesizedExpression
}
