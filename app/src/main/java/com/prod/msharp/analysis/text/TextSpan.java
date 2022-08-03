package com.prod.msharp.analysis.text;

/**
 * This class represents the span of some text.
 */
public class TextSpan {
    public int start;
    public int length;
    public int end;

    public TextSpan(int start, int length) {
        this.start = start;
        this.length = length;
        end = this.start + this.length;
    }

    public static TextSpan createFromBounds(int start, int end) {
        return new TextSpan(start, end - start);
    }
}
