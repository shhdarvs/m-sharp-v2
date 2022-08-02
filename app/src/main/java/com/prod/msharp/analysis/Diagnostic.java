package com.prod.msharp.analysis;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

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
