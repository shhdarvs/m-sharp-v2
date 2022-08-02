package com.prod.ms;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.prod.msharp.analysis.Evaluator;
import com.prod.msharp.analysis.decoration.Decorate;
import com.prod.msharp.analysis.decoration.DecoratedExpression;
import com.prod.msharp.analysis.syntax.AST;
import com.prod.msharp.analysis.syntax.ConsoleColours;
import com.prod.msharp.analysis.syntax.SyntaxTree;
import com.prod.msharp.analysis.syntax.Token;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Program {

    //Commands for user
    // 1. #showTree: toggles AST to show ON/OFF
    // 2. #clear: clears console

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void main(String[] args) {
        boolean showTree = false;

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("> ");

            String line = sc.nextLine();

            if ((line == null) && line.isEmpty() && line.trim().isEmpty())
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
            SyntaxTree syntaxTree = SyntaxTree.parse(line);
            Decorate decorate = new Decorate();
            DecoratedExpression decoratedExpression = decorate.decorateExpression(syntaxTree.root);

            List<String> diagnostics = Stream
                    .concat(syntaxTree.diagnostics.stream(), decorate.diagnostics.stream())
                    .collect(Collectors.toList());

            if (showTree) {
                prettyPrint(syntaxTree.root, "", true);
                System.out.println();
            }


            if (diagnostics.size() > 0) {
                System.out.print(ConsoleColours.TEXT_RED);
                for (String d : diagnostics)
                    System.out.println(d);
                System.out.print(ConsoleColours.TEXT_RESET);
            } else {
                Evaluator eval = new Evaluator(decoratedExpression);
                System.out.println("Result: " + eval.evaluate());
            }

            System.out.println();

        }

    }

    /**
     * This method prints the source program as a abstract syntax tree
     *
     * @param node   the root node of the tree
     * @param indent the amount of indentation to apply at the current point in time
     */
    static void prettyPrint(AST node, String indent, boolean isLast) {
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

