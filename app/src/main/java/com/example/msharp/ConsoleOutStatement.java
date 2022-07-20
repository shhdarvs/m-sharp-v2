package com.example.msharp;

import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Console;
import java.util.Map;
/*Effectively a printLn statement. Will print any expression. */
public class ConsoleOutStatement extends Statement{

    public Expression expression;
    Map<String,Integer> Numbers;
    Map<String, String> Strings;
    Map<String, String> Bools;
    TextView console;

    public ConsoleOutStatement(Expression expression, Map<String,Integer> Numbers, Map<String, String> Strings, Map<String, String> Bools, TextView console)
    {
        this.expression = expression;
        this.Numbers = Numbers;
        this.Bools = Bools;
        this.Strings = Strings;
        this.console = console;
    }


    /*Executes the expression, prints the result based on type. */
    @Override
    public void execute() throws Exception {
        expression.execute();
        switch (expression.type())
        {
            case 0:
            case 3:
            case 6:
                console.append(Integer.toString(expression.resultInt()) + "\r\n");
                break;
            case 1:
            case 2:
            case 4:
            case 5:
                console.append(expression.resultBool()+ "\r\n");
                break;
            case 7:
                String[] stringOut = expression.resultString().split("#",2 );
                console.append(stringOut[1]+ "\r\n");
                break;

        }

    }
}
