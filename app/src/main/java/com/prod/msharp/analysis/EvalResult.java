package com.prod.msharp.analysis;

import java.util.List;

/**
 * This class represents the result of some expression in evaluation. It contains a list of diagnostic errors, and an Object value which represents the result of the evaluation.
 */
public class EvalResult {
    public DiagnosticSet diagnostics;
    public Object value;

    public EvalResult(DiagnosticSet diagnostics, Object value) {
        this.diagnostics = diagnostics;
        this.value = value;
    }
}
