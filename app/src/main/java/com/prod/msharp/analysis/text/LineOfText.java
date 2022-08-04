package com.prod.msharp.analysis.text;

import androidx.annotation.NonNull;

public class LineOfText {
    public SourceText text;
    public int start;
    public int end;
    int length;
    int lengthWithLineBreak;
    public TextSpan span;
    public TextSpan spanWithLineBreak;

    public LineOfText(SourceText text, int start, int length, int lengthWithLineBreak) {
        this.text = text;
        this.start = start;
        this.end = start + length;
        this.length = length;
        this.lengthWithLineBreak = lengthWithLineBreak;
        this.span = new TextSpan(start, length);
        this.spanWithLineBreak = new TextSpan(start, lengthWithLineBreak);
    }

    @NonNull
    @Override
    public String toString() {
        return text.toString(span);
    }


}
