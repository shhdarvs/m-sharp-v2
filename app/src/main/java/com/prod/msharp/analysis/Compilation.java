package com.prod.msharp.analysis;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.prod.msharp.analysis.decoration.Decorate;
import com.prod.msharp.analysis.decoration.DecoratedExpression;
import com.prod.msharp.analysis.syntax.SyntaxTree;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class will act as an API in our compiler. It will be exposed to the main program. It takes a {@link SyntaxTree} object as a parameter in its constructor. The class contains a method {@link Compilation#evaluate(Map)} )} which evaluates the result of some decorated expression.
 */
public class Compilation {
    public SyntaxTree tree;

    public Compilation(SyntaxTree tree) {
        this.tree = tree;
    }

    /**
     * This method evaluates a {@link DecoratedExpression} object. If there are any diagnostic reports, the expression is not evaluated. Otherwise, the expression is evaluated and an object of type {@link EvalResult} is returned containing an empty diagnostics list, and the value of the result.
     *
     * @param variables a Hashmap which maps the name of a variable to an object
     * @return an object of type {@link EvalResult}
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public EvalResult evaluate(Map<VariableSymbol, Object> variables) {
        var decorate = new Decorate(variables); //create and instantiate a Decorate object
        var decoratedExpression = decorate.decorateExpression(tree.root); //decorate (type-check) the root of the tree.

        //Concat any diagnostics from the syntax tree and decorator
        var diagnostics = new DiagnosticSet(Stream
                .concat(tree.diagnostics.diagnostics.stream(), decorate.diagnostics.diagnostics.stream())
                .collect(Collectors.toList()));

        //If the diagnostics is not empty, return an EvalResult object with the list of diagnostics from the SyntaxTree and Decorate class
        if (!diagnostics.isEmpty())
            return new EvalResult(diagnostics, null);

        //If there are errors, evaluate the decorated expression and return an EvalResult with the value of the evaluation as a parameter
        var evaluator = new Evaluator(decoratedExpression, variables);
        return new EvalResult(diagnostics, evaluator.evaluate());

    }
}

