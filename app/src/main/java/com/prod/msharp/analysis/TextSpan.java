package com.prod.msharp.analysis;

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
}
