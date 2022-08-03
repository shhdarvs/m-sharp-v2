package com.prod.msharp.analysis.syntax;

import com.prod.msharp.analysis.DiagnosticSet;
import com.prod.msharp.analysis.text.SourceText;
import com.prod.msharp.analysis.text.TextSpan;

/**
 * This class scans a source program and recognizes tokens in the source program (Lexical Analysis).
 */
public class Lexer {
    public static final String TAG = "Lexer";

    private SourceText text;
    private int length;
    private int pos;
    private char current;

    private int start;
    private TokenKind kind;
    private Object value;

    public DiagnosticSet diagnostics = new DiagnosticSet();

    public Lexer(SourceText text) {
        this.text = text;
        this.pos = -1;
        this.length = text.length();

        next();
    }


    /**
     * This method returns the current integer position of the next character from the input string. If the current character is the last character, current is set to the empty character (EOF)
     */
    private void next() {
        pos++;

        if (pos < length)
            current = text.charAt(pos);
        else
            current = '\0';

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
     * This method advances the current token to be the next token if available
     */
    public Token nextToken() {
        current = current();

        start = pos;
        kind = TokenKind.ERROR;
        value = null;

        switch (current) {
            case '\0':
                kind = TokenKind.EOFToken;
                break;
            case '+':
                kind = TokenKind.PlusToken;
                pos++;
                break;
            case '-':
                kind = TokenKind.MinusToken;
                pos++;
                break;
            case '*':
                kind = TokenKind.MultToken;
                pos++;
                break;
            case '/':
                kind = TokenKind.DivToken;
                pos++;
                break;
            case '(':
                kind = TokenKind.OpenParenthesis;
                pos++;
                break;
            case ')':
                kind = TokenKind.ClosedParenthesis;
                pos++;
                break;
            case '!':
                if (lookahead() == '=') {
                    pos += 2;
                    kind = TokenKind.NotEqualsToken;
                } else {
                    pos++;
                    kind = TokenKind.NotToken;
                }
                break;
            case '&':
                if (lookahead() == '&') {
                    pos += 2;
                    kind = TokenKind.AndToken;
                }
                break;
            case '|':
                if (lookahead() == '|') {
                    pos += 2;
                    kind = TokenKind.OrToken;
                }
                break;
            case '=':
                if (lookahead() == '=') {
                    pos += 2;
                    kind = TokenKind.DoubleEqualsToken;
                }
                break;
            case '<':
                if (lookahead() == '-') {
                    pos += 2;
                    kind = TokenKind.ArrowToken;
                }
                break;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                readNumber();
                break;
            case ' ':
            case '\t':
            case '\n':
            case '\r':
                readWhiteSpace();
                break;
            default:
                if (Character.isLetter(current))
                    readIdentifierOrKeyword();
                else if (Character.isWhitespace(current))
                    readWhiteSpace();
                else {
                    diagnostics.reportIncorrectCharacter(pos, current);
                    pos++;
                }

                break;

        }

        var length = pos - start;
        var text = SyntaxHelper.getText(kind);

        if (text == null)
            text = this.text.toString(start, length);

        return new Token(kind, start, text, value);

    }

    /**
     * Since the language makes use of both integer and double types, this method either makes an integer/double token
     */
    private void readNumber() {
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
        String text = this.text.toString(start, start + length);

        if (dots == 0)
            readInteger(text);
        else
            readDouble(text);

    }

    private void readInteger(String text) {
        int value = 0;
        try {
            value = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            diagnostics.reportInvalidNumber(new TextSpan(start, length), text, Integer.class);
        } finally {
            this.kind = TokenKind.IntegerToken;
            this.value = value;
        }
    }

    private void readDouble(String text) {
        double value = 0;
        try {
            value = Double.parseDouble(text);
        } catch (NumberFormatException e) {
            diagnostics.reportInvalidNumber(new TextSpan(start, length), text, Double.class);
        } finally {
            this.kind = TokenKind.DoubleToken;
            this.value = value;
        }
    }

    private void readWhiteSpace() {
        while (Character.isWhitespace(current))
            next();

        kind = TokenKind.WhitespaceToken;
    }

    private void readIdentifierOrKeyword() {
        while (Character.isLetter(current))
            next();

        int length = pos - start;
        String text = this.text.toString(start, length);
        kind = SyntaxHelper.getKeyword(text);
    }

} //close Lexer
