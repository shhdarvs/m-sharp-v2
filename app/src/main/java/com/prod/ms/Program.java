package com.prod.ms;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.prod.msharp.analysis.Compilation;
import com.prod.msharp.analysis.Diagnostic;
import com.prod.msharp.analysis.DiagnosticSet;
import com.prod.msharp.analysis.VariableSymbol;
import com.prod.msharp.analysis.syntax.AST;
import com.prod.msharp.analysis.syntax.SyntaxTree;
import com.prod.msharp.analysis.syntax.Token;
import com.prod.msharp.analysis.text.TextSpan;

import java.io.Console;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class Program {

    //Commands for user
    // 1. #showTree: toggles AST to show ON/OFF
    // 2. #clear: clears console

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void main(String[] args) {
        boolean showTree = false;
        Map<VariableSymbol, Object> variables = new HashMap<>();
        var stringBuilder = new StringBuilder();

        Scanner sc = new Scanner(System.in);

        while (true) {
            if (stringBuilder.length() == 0)
                System.out.print("> ");
            else
                System.out.print("| ");

            String input = sc.nextLine();

            var emptyLine = input == null || input.isEmpty() || input.trim().isEmpty();

            if (stringBuilder.length() == 0) {
                if (emptyLine)
                    break;
                else if (input.equals("#showTree")) {
                    showTree = !showTree;
                    String output = showTree ? "Now showing AST..." : "Not showing showing AST...";
                    System.out.println(output);
                    System.out.println();
                    continue;
                } else if (input.equals("#clear")) {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    continue;
                }

            }

            stringBuilder.append(input).append(System.getProperty("line.separator"));
            var text = stringBuilder.toString();

            //Parse the source program and decorate the tree
            var syntaxTree = SyntaxTree.parse(text);

            if (!emptyLine)
                continue;

            var compilation = new Compilation(syntaxTree);
            var result = compilation.evaluate(variables);

            DiagnosticSet diagnostics = result.diagnostics;

            if (showTree) {
                prettyPrint(syntaxTree.root, "", true);
                System.out.println();
            }

            if (diagnostics.isEmpty()) {
                System.out.println("Result: " + result.value);
            } else {
                var sourceText = syntaxTree.sourceText;

                for (Diagnostic d : diagnostics.diagnostics) {
                    var lineIndex = sourceText.getLineIndex(d.textSpan.start);
                    var line = sourceText.lines.get(lineIndex);
                    var lineNumber = lineIndex + 1;
                    var ch = d.textSpan.start - line.start + 1;

                    System.out.println();

                    System.out.print(ConsoleColours.TEXT_RED);
                    System.out.printf("(%s, %s): ", lineNumber, ch);
                    System.out.println(d); //print out diagnostics
                    System.out.print(ConsoleColours.TEXT_RESET);

                    var prefixSpan = TextSpan.createFromBounds(line.start, d.textSpan.start);
                    var suffixSpan = TextSpan.createFromBounds(d.textSpan.end, line.end);

                    var prefix = sourceText.toString(prefixSpan);
                    var error = sourceText.toString(d.textSpan);
                    var suffix = sourceText.toString(suffixSpan);

                    //Print prefix
                    System.out.print("    ");
                    System.out.print(prefix);

                    //Print error in red
                    System.out.print(ConsoleColours.TEXT_RED);
                    System.out.print(error);
                    System.out.print(ConsoleColours.TEXT_RESET);

                    //Print suffix
                    System.out.print(suffix);
                }

                System.out.println();
                System.out.println();

            }

            stringBuilder.setLength(0); //clear string builder
            System.out.println();

        }


    }

    private static void prettyPrint(AST node, String indent, boolean isLast) {
        // └──
        // ├──
        // │

        String marker = isLast ? "└──" : "├──";


        System.out.print(indent);
        System.out.print(marker);
        System.out.print(node.kind());

        if (node instanceof Token) {
            Token t = (Token) node;

            if (t.value != null) {
                System.out.print(" ");
                System.out.print(t.value);

            }

        }

        System.out.println();


        indent += isLast ? "    " : "│   ";

        AST lastChild = null;

        if (node.getChildren().size() > 0)
            lastChild = node.getChildren().get(node.getChildren().size() - 1);

        for (AST child : node.getChildren())
            prettyPrint(child, indent, child == lastChild);
    }

}

