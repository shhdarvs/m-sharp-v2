package com.example.msharp22;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.msharp.EditorActivity;

import java.io.Console;
import java.util.Scanner;

public class Program {

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("> ");

            String line = sc.nextLine();

            if ((line == null) && line.isEmpty() && line.trim().isEmpty())
                return;

            SyntaxTree syntaxTree = SyntaxTree.parse(line);

            prettyPrint(syntaxTree.root, "", true);
            System.out.println();

            if (syntaxTree.diagnostics.size() > 0) {
                System.out.print(ConsoleColours.TEXT_RED);
                for (String d : syntaxTree.diagnostics)
                    System.out.println(d);
                System.out.print(ConsoleColours.TEXT_RESET);
            } else {
                Evaluator eval = new Evaluator(syntaxTree.root);
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

        indent += isLast ? "    " : "│    ";

        AST lastChild = null;

        if (node.getChildren().size() > 0)
            lastChild = node.getChildren().get(node.getChildren().size() - 1);

        for (AST child : node.getChildren())
            prettyPrint(child, indent, child == lastChild);

    }
}

