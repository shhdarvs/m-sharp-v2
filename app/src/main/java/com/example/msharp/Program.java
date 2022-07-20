package com.example.msharp;

import android.util.Log;
import android.widget.TextView;

import com.example.msharp.statements.Statement;

import java.util.ArrayList;

/* The program class stores all the statements that make up the program */

/**
 * The Program class inherits from the AST class. A program is a list of statements. When run, the program executes each statement in order.
 */
public class Program extends AST {
    public static final String TAG = "Program";
    public ArrayList<Statement> statements;

    public Program() {
        statements = new ArrayList<>();
    }

    public void add(Statement newStatement) {
        statements.add(newStatement);
    }

    /**
     * This method loops over the list of statements belonging to this programming and executes each statement one at a time.
     *
     * @param textView This textview will display any errors caught on execution
     */
    public void run(TextView textView) {
        for (Statement s : statements)
            try {
                s.execute();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                System.out.println(TAG + ": " + e.getMessage());
                textView.append("Error: " + e.getMessage() + "\r\n"); // print error
            }

    }

}
