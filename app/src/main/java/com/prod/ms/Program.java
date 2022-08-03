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

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("> ");

            String line = sc.nextLine();

            if ((line == null) || line.isEmpty() || line.trim().isEmpty())
                return;

            if (line.equals("#showTree")) {
                showTree = !showTree;
                String output = showTree ? "Now showing AST..." : "Not showing showing AST...";
                System.out.println(output);
                System.out.println();
                continue;
            } else if (line.equals("#clear")) {
                System.out.print("\033[H\033[2J");
                System.out.flush();
                continue;
            }

            //Parse the source program and decorate the tree
            var syntaxTree = SyntaxTree.parse(line);
            var compilation = new Compilation(syntaxTree);
            var result = compilation.evaluate(variables);

            DiagnosticSet diagnostics = result.diagnostics;

            if (showTree) {
                prettyPrint(syntaxTree.root, "", true);
                System.out.println();
            }

            if (!diagnostics.isEmpty()) {
                System.out.print(ConsoleColours.TEXT_RED);
                for (Diagnostic d : diagnostics.diagnostics)
                    System.out.println(d);
                System.out.print(ConsoleColours.TEXT_RESET);
            } else
                System.out.println("Result: " + result.value);

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

