package com.prod.msharp.analysis;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.prod.msharp.analysis.syntax.TokenKind;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class represents a set of diagnostics. It contains one field which is a list of {@link Diagnostic} objects. There are various report methods which can be invoked to report different errors. * @author Jarod Darvel
 */
public class DiagnosticSet {
    public List<Diagnostic> diagnostics = new ArrayList<>();

    public DiagnosticSet() {
    }

    public DiagnosticSet(List<Diagnostic> diagnostics) {
        this.diagnostics = diagnostics;
    }

    /**
     * This method adds a list of {@link Diagnostic} objects to the {@link DiagnosticSet#diagnostics} field of this class.
     *
     * @param diagnostics the list of {@link Diagnostic} objects to be added
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addAll(DiagnosticSet diagnostics) {
        this.diagnostics = Stream
                .concat(this.diagnostics.stream(), diagnostics.diagnostics.stream())
                .collect(Collectors.toList());
    }

    /**
     * This method creates a new {@link Diagnostic} object and adds it to the list of diagnostics
     *
     * @param textSpan the {@link TextSpan} object of the diagnostic
     * @param message  the message describing the diagnostic
     */
    private void report(TextSpan textSpan, String message) {
        var diagnostic = new Diagnostic(textSpan, message);
        diagnostics.add(diagnostic);
    }

    /**
     * This method reports an invalid number error. This error occurs when the expected number type does not match the given number type.
     *
     * @param textSpan the {@link TextSpan} object of the diagnostic
     * @param text     the text which represents the number
     * @param type     the expected type of the number
     */
    public void reportInvalidNumber(TextSpan textSpan, String text, Type type) {
        report(textSpan, String.format("The number %s is not of type %s", text, type));
    }

    /**
     * This method reports an incorrect character error. This error occurs when the input character is unknown.
     *
     * @param pos the position of the character
     * @param c   the input character
     */
    public void reportIncorrectCharacter(int pos, char c) {
        report(new TextSpan(pos, 1), String.format("Incorrect character input '%s'", c));
    }

    /**
     * This method reports an unexpected token error. This error occurs when the token kind of the current token does not match the expected token kind.
     *
     * @param textSpan     the {@link TextSpan} object of the diagnostic
     * @param tokenKind    the {@link TokenKind} object which represents the kind of the input token
     * @param expectedKind the {@link TokenKind} object which represents the expected token kind
     */
    public void reportUnexpectedToken(TextSpan textSpan, TokenKind tokenKind, TokenKind expectedKind) {
        report(textSpan, String.format("Unexpected token <%s>, expected <%s>", tokenKind, expectedKind));
    }

    /**
     * This method reports a undefined unary operator error. This error occurs when an undefined unary operator is applied to an operand.
     *
     * @param textSpan    the span of the unary operator
     * @param operator    the string representation of the operator
     * @param operandType the type of the operand
     */
    public void reportUndefinedUnaryOperator(TextSpan textSpan, String operator, Type operandType) {
        report(textSpan, String.format("Unary operator %s is not defined for type %s", operator, operandType));
    }

    /**
     * This method reports a undefined binary operator error. This error occurs when an undefined binary operator is applied to a binary expresson.
     *
     * @param textSpan         the span of the unary operator
     * @param operator         the string representation of the operator
     * @param leftOperandType  the type of the left operand
     * @param rightOperandType the type of the right operand
     */
    public void reportUndefinedBinaryOperator(TextSpan textSpan, String operator, Type leftOperandType, Type rightOperandType) {
        report(textSpan, String.format("Binary operator %s is not defined for types %s and %s", operator, leftOperandType, rightOperandType));
    }

    /**
     * This method returns true if the diagnostic set is empty, and false if the diagnostic set contains items.
     *
     * @return true if the diagnostic set is empty, and false if the diagnostic set contains items
     */
    public boolean isEmpty() {
        return diagnostics.isEmpty();
    }
}
