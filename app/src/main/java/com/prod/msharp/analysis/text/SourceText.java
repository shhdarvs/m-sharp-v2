package com.prod.msharp.analysis.text;

import androidx.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the source text of a program
 */
public class SourceText {
    public String text;
    public List<LineOfText> lines;

    public static SourceText from(String text) {
        return new SourceText(text);
    }

    private SourceText(String text) {
        this.text = text;
        lines = parseLines(this, text); //parse the source program line by line
    }

    public char charAt(int index) {
        return text.charAt(index);
    }

    public int length() {
        return text.length();
    }

    /**
     * This method uses a binary search to get the line index of where an error has occurred.
     * @param pos the position the character on the line which has invoked the error
     * @return the index of the line on which the error occurred
     */
    public int getLineIndex(int pos) {
        var lower = 0;
        var upper = lines.size() - 1;

        while (lower <= upper) {
            var index = lower + (upper - lower) / 2;
            var start = lines.get(index).start;

            if (pos == start)
                return index;

            if (start > pos)
                upper = index - 1;
            else
                lower = index + 1;
        }

        return lower - 1;
    }

    /**
     * This method parses source text of multiple lines. It returns an immutable list of {@link LineOfText} objects which represent each parsed line.
     *
     * @param sourceText an object of type {@link SourceText} which represents the source text of the program
     * @param text       the source program as a string representation
     * @return a list containing {@link LineOfText} objects
     */
    private static List<LineOfText> parseLines(SourceText sourceText, String text) {
        var result = new ArrayList<LineOfText>();

        var pos = 0;
        var lineStart = 0;

        while (pos < text.length()) {
            var width = getLineBreakWidth(text, pos);

            if (width == 0) {
                pos++;
            } else {
                addLine(result, sourceText, pos, lineStart, width);

                pos += width;
                lineStart = pos;
            }
        }

        if (pos > lineStart)
            addLine(result, sourceText, pos, lineStart, 0);

        return List.copyOf(result);
    }

    private static int getLineBreakWidth(String text, int i) {
        var ch = text.charAt(i);

        var lookahead = i + 1 >= text.length() ? '\0' : text.charAt(i + 1);

        if (ch == '\r' && lookahead == '\n')
            return 2;
        if (ch == '\r' || ch == '\n')
            return 1;
        return 0;

    }

    private static void addLine(List<LineOfText> result, SourceText sourceText, int pos, int lineStart, int width) {
        var lengthOfLine = pos - lineStart;
        var lengthWithLineBreak = lengthOfLine + width;
        result.add(new LineOfText(sourceText, lineStart, lengthOfLine, lengthWithLineBreak));

    }

    @NonNull
    @Override
    public String toString() {
        return text;
    }

    public String toString(int start, int length) {
        return text.substring(start, start + length);
    }

    public String toString(TextSpan span) {
        return toString(span.start, span.length);
    }

}

