package com.prod.msharp.analysis;

import androidx.annotation.NonNull;

import com.prod.msharp.analysis.text.TextSpan;

public class Diagnostic {
    public TextSpan textSpan;
    String message;

    public Diagnostic(TextSpan textSpan, String message) {
        this.textSpan = textSpan;
        this.message = message;
    }

    @NonNull
    @Override
    public String toString() {
        return message;
    }

}
