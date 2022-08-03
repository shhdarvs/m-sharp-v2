package com.prod.msharp.analysis.syntax;

public class SyntaxHelper {

    /**
     * This method returns the precedence of an operator token for a unary expression. Higher precedence is given to + and - operators for unary expressions
     *
     * @param kind the kind of operator token
     * @return an integer value representing the precedence
     */
    static int getUnaryOperatorPrecedence(TokenKind kind) {
        switch (kind) {
            case PlusToken:
            case MinusToken:
            case NotToken:
                return 6;

            // If the operator is not a unary operator, return precedence of 0
            default:
                return 0;
        }
    }

    /**
     * This method returns the precedence of an operator token for a binary expression. Higher precedence is given to * and / operators for binary expressions.
     *
     * @param kind the kind of operator token
     * @return an integer value representing the precedence
     */
    static int getBinaryOperatorPrecedence(TokenKind kind) {
        switch (kind) {
            // Higher precedence is given to the * and / operators
            case MultToken:
            case DivToken:
                return 5;

            case PlusToken:
            case MinusToken:
                return 4;

            case DoubleEqualsToken:
            case NotEqualsToken:
                return 3;

            case AndToken:
                return 2;

            case OrToken:
                return 1;

            // If the operator is not a binary operator, return precedence of 0
            default:
                return 0;
        }
    }

    static TokenKind getKeyword(String text) {
        switch (text) {
            case "true":
                return TokenKind.TrueKeyword;
            case "false":
                return TokenKind.FalseKeyword;
            default:
                return TokenKind.IdentifierToken;
        }
    }

    public static String getText(TokenKind kind) {
        switch (kind) {
            case PlusToken:
                return "+";
            case MinusToken:
                return "-";
            case MultToken:
                return "*";
            case DivToken:
                return "/";
            case NotToken:
                return "!";
            case ArrowToken:
                return "<-";
            case AndToken:
                return "&&";
            case OrToken:
                return "||";
            case DoubleEqualsToken:
                return "==";
            case NotEqualsToken:
                return "!=";
            case OpenParenthesis:
                return "(";
            case ClosedParenthesis:
                return ")";
            case TrueKeyword:
                return "true";
            case FalseKeyword:
                return "false";
            default:
                return null;
        }
    }
}
