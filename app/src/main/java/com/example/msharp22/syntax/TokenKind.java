package com.example.msharp22.syntax;

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
    NotToken,
    AndToken,
    OrToken,
    EqualsToken,
    NotEqualsToken,
    OpenParenthesis,
    ClosedParenthesis,
    IdentifierToken,

    //Keywords,
    TrueKeyword,
    FalseKeyword,

    //Expressions,
    Literal,
    BinaryExpression,
    UnaryExpression,
    ParenthesizedExpression
}
