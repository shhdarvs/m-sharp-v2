package com.prod.msharp.analysis.syntax;

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
    ArrowToken,
    AndToken,
    OrToken,
    DoubleEqualsToken,
    NotEqualsToken,
    OpenParenthesis,
    ClosedParenthesis,
    IdentifierToken,

    //Keywords,
    TrueKeyword,
    FalseKeyword,

    //Expressions,
    LiteralExpression,
    NameExpression,
    AssignmentExpression,
    BinaryExpression,
    UnaryExpression,
    ParenthesizedExpression
}
