

package com.example.msharp;

//import javax.print.attribute.standard.Finishings;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import java.io.Console;
import java.util.Scanner;

public class ReadInExpression extends Expression
{
    public String readInLine;
    public int type;
    public int intResult;
    public String boolResult;
    public String stringResult;
    public Context context;
    private boolean resultValue;

    public ReadInExpression (Context context)
    {
        this.readInLine = "";
        type = -1;
        intResult = -1;
        boolResult = null;
        stringResult = null;
        this.context = context;

    }

    /*Evaluate expression. In this case will ask for an input, and store it as a var based on its type. */
    @Override
    public void execute() throws Exception {

        final Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message mesg)
            {
                throw new RuntimeException();
            }
        };

        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle("Read in");


        // Set an EditText view to get user input
        final EditText input = new EditText(context);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();

                readInLine = value;

                handler.sendMessage(handler.obtainMessage());

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                handler.sendMessage(handler.obtainMessage());
            }
        });

        AlertDialog dialog = alert.create();
        Window window = dialog.getWindow();
        dialog.getWindow().setDimAmount(0f);
        WindowManager.LayoutParams layoutParams=window.getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);
        dialog.show();
       // alert.show();

        try{
            Looper.loop();
        }
        catch (RuntimeException e) {}


        Functions fun = new Functions();
        if(fun.isInteger(readInLine))
        {
            intResult = Integer.parseInt(readInLine);
            type = 6;
        }
        else if(fun.isBool(readInLine))
        {
            boolResult = readInLine;
            type = 5;
        }
        else if(fun.isString(readInLine))//its a string
        {
            stringResult = readInLine;
            type = 7;
        }
        else
        {
            Exception e = new Exception("invalid factor read in");
            throw e;

        }


    }

    @Override
    public int type() {
        return type;
    }

    @Override
    public int resultInt() {
        return intResult;
    }

    @Override
    public String resultBool() {
        return boolResult;
    }

    @Override
    public String resultString() {
        return stringResult;
    }
}
