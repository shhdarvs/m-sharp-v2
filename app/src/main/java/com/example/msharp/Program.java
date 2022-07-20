package com.example.msharp;

import android.widget.TextView;

import java.util.ArrayList;

/* The program class stores all the statements that make up the program */
public class Program extends AST
{

    public ArrayList<Statement> statements;

    public Program()
    {
        statements = new ArrayList<Statement>();
    }

    public void Add(Statement newStatement)
    {
        statements.add(newStatement);
    }

    /* Runs the program, executing each statement in the program one by one for the list of statements it has stored */
    public void Run(TextView textView)
    {
        for(int x = 0; x < statements.size(); x++)
        {//
            try
            {
                Statement currentStatement = statements.get(x);
                currentStatement.execute();
            }
            catch (Exception e)
            {
                textView.append("Error: " + e.getMessage() + "\r\n"); // print error
            }
        }
    }

}
