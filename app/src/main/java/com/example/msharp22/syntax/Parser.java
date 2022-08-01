package com.example.msharp22.syntax;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the parser for the language grammar. Given an input string, this determines if the input string is part of the grammar
 */
@RequiresApi(api = Build.VERSION_CODES.R)
public class Parser {
    public static final String TAG = "NewParser";

    private String text;
    private final Token[] tokens;
    public int pos;

    private Token current;

    private List<String> diagnostics = new ArrayList<>();


    /**
     * In the constructor, the text is processed by the lexer, and an array of tokens are generated
     *
     * @param text source program to be parsed
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public Parser(String text) {
        this.text = text;

        List<Token> tokens = new ArrayList<>();
        Lexer lexer = new Lexer(text);
        Token token;

        do {
            token = lexer.nextToken();

            if (token.kind != TokenKind.WhitespaceToken && token.kind != TokenKind.ERROR)
                tokens.add(token);

        } while (token.kind != TokenKind.EOFToken);

        this.tokens = tokens.toArray(new Token[0]);
        diagnostics.addAll(lexer.getDiagnostics());
    }

    /**
     * A helper method to look ahead to see what is the next token which will be parsed
     *
     * @param ahead offset value of the look ahead
     * @return the token at the look ahead value
     */
    private Token peek(int ahead) {
        int index = pos + ahead;

        if (index >= tokens.length)
            return tokens[tokens.length - 1];

        return tokens[index];
    }

    /**
     * This method returns the current token
     *
     * @return the current token
     */
    private Token current() {
        current = peek(0);
        return current;
    }

    /**
     * This method consumes the current token while also moving onto the next token
     *
     * @return the current Token being consumed
     */
    private Token next() {
        Token current = current();
        pos++;
        return current;
    }

    private Token match(TokenKind kind) {
        if (current.kind == kind)
            return next();

        diagnostics.add(String.format("ERROR: unexpected token <%s>, expected <%s>", current.kind, kind));
        return new Token(kind, current.pos, null, null);
    }

    public List<String> getDiagnostics() {
        return diagnostics;
    }

    /**
     * This method parses and builds an AST. It ensures operator precedence. It makes use of a recursive descent parser
     *
     * @return an object of type SyntaxTree which represents the parsed expression
     */
    public SyntaxTree parse() {
        Expression exp = parseExpression(0); // 0 is default for parent precedence
        Token EOFToken = match(TokenKind.EOFToken);
        return new SyntaxTree(exp, EOFToken, diagnostics);
    }


    private Expression parseExpression(int parentPrecedence) {
        Expression left;

        int unaryPrecedence = SyntaxHelper.getUnaryOperatorPrecedence(current().kind);

        if (unaryPrecedence != 0 && unaryPrecedence >= parentPrecedence) {
            Token operator = next();
            Expression operand = parseExpression(unaryPrecedence);
            left = new UnaryExpression(operator, operand);
        } else
            left = parsePrimaryExpression();

        while (true) {
            int binaryPrecedence = SyntaxHelper.getBinaryOperatorPrecedence(current().kind);

            if (binaryPrecedence == 0 || binaryPrecedence <= parentPrecedence)
                break;

            Token operator = next();
            Expression right = parseExpression(binaryPrecedence);
            left = new BinaryExpression(left, operator, right);
        }

        return left;
    }


    /**
     * This method parses a primary expression. A primary expression can either be an integer literal, double literal or an open parenthesis.
     *
     * @return an object of type NumberExpression which represents the parsed literal
     */
    private Expression parsePrimaryExpression() {
        switch (current.kind) {
            case OpenParenthesis:
                Token left = next();
                Expression e = parseExpression(0);
                Token right = match(TokenKind.ClosedParenthesis);
                return new ParenthesizedExpression(left, e, right);

            case IntegerToken:
                Token numToken = match(TokenKind.IntegerToken);
                return new LiteralExpression(numToken, numToken.value);

            case DoubleToken:
                numToken = match(TokenKind.DoubleToken);
                return new LiteralExpression(numToken, numToken.value);

            case TrueKeyword:
            case FalseKeyword:
                Token keyword = next();
                boolean value = keyword.kind == TokenKind.TrueKeyword;
                return new LiteralExpression(current, value);
        }

        return new LiteralExpression(new Token(TokenKind.ERROR, current.pos, null, null), null);

    }

}

