package com.example.msharp22.syntax;

import java.util.ArrayList;
import java.util.List;

/**
 * This class scans a source program and recognizes tokens in the source program (Lexical Analysis).
 */
public class Lexer {
    public static final String TAG = "Lexer";

    private String text;
    private int length;
    private int pos;
    private char current;

    private List<String> diagnostics = new ArrayList<>();

    public Lexer(String text) {
        this.text = text;
        this.pos = -1;
        this.length = text.length();

        next();
    }


    /**
     * This method returns the current integer position of the next character from the input string. If the current character is the last character, current is set to the empty character (EOF)
     *
     * @return the current integer position of the current character
     */
    private int next() {
        pos++;

        if (pos < length)
            current = text.charAt(pos);
        else
            current = '\0';

        return pos;
    }

    private char peek(int offset) {
        int index = pos + offset;

        if (index < length)
            return text.charAt(index);
        else
            return '\0';
    }

    private char current() {
        return peek(0);
    }

    private char lookahead() {
        return peek(1);
    }

    /**
     * This method returns an iterable over the array list named diagnostics
     *
     * @return an iterable over diagnostics
     */
    public List<String> getDiagnostics() {
        return diagnostics;
    }

    /**
     * This method advances the current token to be the next token if available
     */
    public Token nextToken() {
        current = current();

        if (pos >= length)
            return new Token(TokenKind.EOFToken, pos, "\0", null);

        if (Character.isDigit(current)) {
            return makeIntOrDouble();
        }

        if (Character.isWhitespace(current)) {
            int start = pos;

            while (Character.isWhitespace(current))
                next();

            int length = pos - start;
            String text = this.text.substring(start, start + length);
            return new Token(TokenKind.WhitespaceToken, start, text, null);
        }

        if (Character.isLetter(current)) {
            int start = pos;

            while (Character.isLetter(current))
                next();

            int length = pos - start;
            String text = this.text.substring(start, start + length);
            TokenKind kind = SyntaxHelper.getKeyword(text);

            return new Token(kind, start, text, null);
        }

        switch (current) {
            case '+':
                return new Token(TokenKind.PlusToken, next(), "+", null);
            case '-':
                return new Token(TokenKind.MinusToken, next(), "-", null);
            case '*':
                return new Token(TokenKind.MultToken, next(), "*", null);
            case '/':
                return new Token(TokenKind.DivToken, next(), "/", null);
            case '(':
                return new Token(TokenKind.OpenParenthesis, next(), "(", null);
            case ')':
                return new Token(TokenKind.ClosedParenthesis, next(), ")", null);
            case '!':
                return new Token(TokenKind.NotToken, next(), "!", null);
            case '&':
                if (lookahead() == '&')
                    return new Token(TokenKind.AndToken, pos += 2, "&&", null);
                break;
            case '|':
                if (lookahead() == '|')
                    return new Token(TokenKind.OrToken, pos += 2, "||", null);
                break;

        }

        diagnostics.add(String.format("ERROR: incorrect character input: %s", current));
        return new Token(TokenKind.ERROR, next(), this.text.substring(this.pos - 1, (this.pos - 1) + 1), null);
    }

    /**
     * Since the language makes use of both integer and double types, this method either makes an integer/double token
     *
     * @return a token which is either an integer or double token
     */
    private Token makeIntOrDouble() {
        int start = pos;

        StringBuilder num = new StringBuilder();
        int dots = 0;

        while (current != '\0' && (Character.isDigit(current) || current == '.')) {
            if (current == '.') {
                if (dots == 1)
                    break;

                dots++;
                num.append('.');
            } else
                num.append(current);

            next();
        }

        int length = pos - start;
        String text = this.text.substring(start, start + length);

        if (dots == 0)
            return new Token(TokenKind.IntegerToken, start, text, Integer.parseInt(text));
        else
            return new Token(TokenKind.DoubleToken, start, text, Double.parseDouble(text));
    }
}
