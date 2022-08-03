package com.prod.msharp.analysis.syntax;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.prod.msharp.analysis.Diagnostic;
import com.prod.msharp.analysis.DiagnosticSet;
import com.prod.msharp.analysis.text.SourceText;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Source;

/**
 * This class represents the syntax tree of the source program. This class exists to ensure that an end-of-file (EOF) token has been reached, and that there is no garbage in the tree.
 */
public class SyntaxTree {
    public SourceText sourceText;
    public Expression root;
    public Token EOFToken;
    public DiagnosticSet diagnostics;

    public SyntaxTree(SourceText sourceText, Expression root, Token EOFToken, DiagnosticSet diagnostics) {
        this.sourceText = sourceText;
        this.root = root;
        this.EOFToken = EOFToken;
        this.diagnostics = diagnostics;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static SyntaxTree parse(String text) {
        var sourceText = SourceText.from(text);
        return parse(sourceText);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private static SyntaxTree parse(SourceText sourceText) {
        Parser p = new Parser(sourceText);
        return p.parse();
    }

    public static List<Token> parseTokens(String text) {
        var sourceText = SourceText.from(text);
        return parseTokens(sourceText);

    }

    public static List<Token> parseTokens(SourceText sourceText) {
        List<Token> tokens = new ArrayList<>();
        var lexer = new Lexer(sourceText);

        while (true) {
            var token = lexer.nextToken();

            if (token.kind == TokenKind.EOFToken)
                break;

            tokens.add(token);
        }

        return tokens;
    }
}
