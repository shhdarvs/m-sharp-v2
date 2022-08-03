package com.prod.msharp.analysis.syntax;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.prod.msharp.analysis.DiagnosticSet;

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

    public DiagnosticSet diagnostics = new DiagnosticSet();

    /**
     * In the constructor, the text is processed by the lexer, and an array of tokens are generated
     *
     * @param text source program to be parsed
     */
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
        diagnostics.addAll(lexer.diagnostics);
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
    private Token nextToken() {
        Token current = current();
        pos++;
        return current;
    }

    private Token matchToken(TokenKind kind) {
        if (current.kind == kind)
            return nextToken();

        diagnostics.reportUnexpectedToken(current.getSpan(), current.kind, kind);
        return new Token(kind, current.pos, current.text, null);
    }

    /**
     * This method parses and builds an AST. It ensures operator precedence. It makes use of a recursive descent parser
     *
     * @return an object of type SyntaxTree which represents the parsed expression
     */
    public SyntaxTree parse() {
        Expression exp = parseExpression(); // 0 is default for parent precedence
        Token EOFToken = matchToken(TokenKind.EOFToken);
        return new SyntaxTree(exp, EOFToken, diagnostics);
    }

    private Expression parseExpression() {
        return parseAssignmentExpression();
    }

    private Expression parseAssignmentExpression() {

        if (peek(0).kind == TokenKind.IdentifierToken && peek(1).kind == TokenKind.ArrowToken) {
            var identifierToken = nextToken();
            var operatorToken = nextToken();
            var right = parseAssignmentExpression();
            return new AssignmentExpression(identifierToken, operatorToken, right);
        }

        return parseBinaryExpression(0);
    }

    /**
     * This method parses a binary expression, i.e. an expression with two operands separated by an operand. The manner in which the expression is parsed is dependent on the precedence of the operators involved.
     *
     * @param parentPrecedence the precedence of the parent expression
     * @return an object of type {@link Expression} that represents the parsed binary expression
     */
    private Expression parseBinaryExpression(int parentPrecedence) {
        Expression left;

        int unaryPrecedence = SyntaxHelper.getUnaryOperatorPrecedence(current().kind);

        if (unaryPrecedence != 0 && unaryPrecedence >= parentPrecedence) {
            Token operator = nextToken();
            Expression operand = parseBinaryExpression(unaryPrecedence);
            left = new UnaryExpression(operator, operand);
        } else
            left = parsePrimaryExpression();

        while (true) {
            int binaryPrecedence = SyntaxHelper.getBinaryOperatorPrecedence(current().kind);

            if (binaryPrecedence == 0 || binaryPrecedence <= parentPrecedence)
                break;

            Token operator = nextToken();
            Expression right = parseBinaryExpression(binaryPrecedence);
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
                return parseParenthesizedExpression();

            case IntegerToken:
                return parseIntegerLiteral();

            case DoubleToken:
                return parseDoubleLiteral();

            case TrueKeyword:
            case FalseKeyword:
                return parseBooleanLiteral();

            case IdentifierToken:
            default:
                return parseNameExpression();
        }

    }

    private Expression parseParenthesizedExpression() {
        var left = matchToken(TokenKind.OpenParenthesis);
        var exp = parseAssignmentExpression();
        var right = matchToken(TokenKind.ClosedParenthesis);
        return new ParenthesizedExpression(left, exp, right);
    }

    private Expression parseIntegerLiteral() {
        var numToken = matchToken(TokenKind.IntegerToken);
        return new LiteralExpression(numToken, numToken.value);
    }

    private Expression parseDoubleLiteral() {
        var numToken = matchToken(TokenKind.DoubleToken);
        return new LiteralExpression(numToken, numToken.value);
    }

    private Expression parseBooleanLiteral() {
        var keyword = current.kind == TokenKind.TrueKeyword
                ? matchToken(TokenKind.TrueKeyword)
                : matchToken(TokenKind.FalseKeyword);
        return new LiteralExpression(keyword, current.kind == TokenKind.TrueKeyword);
    }

    private Expression parseNameExpression() {
        var identifierToken = matchToken(TokenKind.IdentifierToken);
        return new NameExpression(identifierToken);
    }

}

