package com.example.msharp22.syntax;

public class SyntaxHelper {

    /**
     * This method returns the precedence of an operator token for a unary expression. Higher precedence is given to + and - operators for unary expressions
     * @param kind the kind of operator token
     * @return an integer value representing the precedence
     */
    static int getUnaryOperatorPrecedence(TokenKind kind) {
        switch (kind) {
            case PlusToken:
            case MinusToken:
                return 3;

            // If the operator is not a binary operator, return precedence of 0
            default:
                return 0;
        }
    }

    /**
     * This method returns the precedence of an operator token for a binary expression. Higher precedence is given to * and / operators for binary expressions.
     * @param kind the kind of operator token
     * @return an integer value representing the precedence
     */
    static int getBinaryOperatorPrecedence(TokenKind kind) {
        switch (kind) {
            // Higher precedence is given to the * and / operators
            case MultToken:
            case DivToken:
                return 2;

            case PlusToken:
            case MinusToken:
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

}
