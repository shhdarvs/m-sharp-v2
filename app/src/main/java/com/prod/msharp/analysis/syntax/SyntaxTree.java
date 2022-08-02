package com.prod.msharp.analysis.syntax;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.prod.msharp.analysis.Diagnostic;
import com.prod.msharp.analysis.DiagnosticSet;

import java.util.List;

/**
 * This class represents the syntax tree of the source program. This class exists to ensure that an end-of-file (EOF) token has been reached, and that there is no garbage in the tree.
 */
public class SyntaxTree {
    public Expression root;
    public Token EOFToken;
    public DiagnosticSet diagnostics;

    public SyntaxTree(Expression root, Token EOFToken, DiagnosticSet diagnostics) {
        this.root = root;
        this.EOFToken = EOFToken;
        this.diagnostics = diagnostics;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static SyntaxTree parse(String text) {
        Parser p = new Parser(text);
        return p.parse();
    }
}
