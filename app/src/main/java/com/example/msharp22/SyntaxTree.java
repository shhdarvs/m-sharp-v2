package com.example.msharp22;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.List;

/**
 * This class represents the syntax tree of the source program. This class exists to ensure that an end-of-file (EOF) token has been reached, and that there is no garbage in the tree.
 */
public class SyntaxTree {
    public Expression root;
    public Token EOFToken;
    public List<String> diagnostics;

    public SyntaxTree(Expression root, Token EOFToken, List<String> diagnostics) {
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
