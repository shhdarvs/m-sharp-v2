package com.example.msharp;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

public class Functions {

    /* Function to check if given input starts with '#', making it a string. */
    public boolean isString(String input)
    {
        char[] allChars = input.toCharArray();
        if(allChars[0] == '#')
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /* Function to check if given input is an existing variable in any of the variable arrays. */
    public boolean isExistingVariable(String input, Map<String,Integer> Numbers, Map<String, String> Strings, Map<String, String> Bools) throws Exception {
        if(Numbers.containsKey(input))
        {
            return true;
        }
        else if(Strings.containsKey(input))
        {
            return true;
        }
        else if(Bools.containsKey(input))
        {
            return true;
        }
        else
        {
            Exception e = new Exception("Variable '" + input + "' does not exist");
            throw  e;

        }
    }

    /*Function to check if any code blocks still have their placeholder values. */
    public boolean checkNoCodeUnchanged(ArrayList<String> loadedLines)
    {
        for(int x = 0; x < loadedLines.size(); x++)
        {
            String Tokens[] = loadedLines.get(x).split(" ");
            for(int y = 0; y < Tokens.length; y++)
            {
                if(Tokens[y].equals("COND") || Tokens[y].equals("EXP") || Tokens[y].equals("VAR"))
                {
                    return false;
                }
            }

        }
        return true;
    }


    /*Check a string can be parsed as a valid expression. Do a local contextual analysis.*/
    public boolean checkValidExpression (String input) throws Exception {


        if(isString(input))
        {
        return true;
        }
        if(isBool(input))
        {
            return true;
        }
        if(isInteger(input))
        {
            return true;
        }
        char[] chars = input.toCharArray();
        if (Character.isDigit(chars[0]))
        {
            Exception o = new Exception("Variables cannot start with a number");
            throw o;
        }
        if(input.indexOf('#') != -1)
        {
            Exception o = new Exception("Variables cannot contain '#' character");
            throw o;
        }
        if(input.indexOf(' ') == -1) //its a variable
        {
            return true;
        }
        try{
            String[] expression = input.split(" ",3 ); //is an expression with an operator
        }
        catch (Exception e)
        {
            Exception o = new Exception("Expressions with multiple terms must be of the form 'Factor Operator Factor'");
            throw o;
        }
        String[] expression = input.split(" ",3 );

        /* Is the left hand side of the expression an int or an existing variable? */

        if(expression[2].indexOf(' ') != -1)
        {
            Exception e = new Exception("Too many arguments");
            throw e;
        }

        if(isBool(expression[0]))
        {
            if(isInteger(expression[2]))
            {
                Exception e = new Exception("Factor types must match.");
                throw e;
            }
        }

        if(isInteger(expression[0]))
        {
            if(isBool(expression[2]))
            {
                Exception e = new Exception("Factor types must match.");
                throw e;
            }
        }

        switch(expression[1])
        {
            case "+":
            case "-":
            case "*":
            case "%":
            case "^":
            case "/":
            case "<":
            case ">":
            case "<=":
            case ">=":

                if(isBool(expression[0]))
                {
                    Exception e = new Exception("Invalid operator");
                    throw e;
                }
                break;

            case "==":

            case "!=":

                break;

            case "&&":

            case "||":

                if(isInteger(expression[0]))
                {
                    Exception e = new Exception("Invalid operator");
                    throw e;
                }
                break;

            default:
                Exception e = new Exception("Invalid operator");
                throw e;


        }
        return true;
    }

    public boolean isValidVariable(String var) throws Exception {
        char[] chars = var.toCharArray();
        if (Character.isDigit(chars[0]))
        {
            Exception o = new Exception("Variables cannot start with a number");
            throw o;
        }
        if(var.indexOf('#') != -1)
        {
            Exception o = new Exception("Variables cannot contain '#' character");
            throw o;
        }
        return true;
    }

    /*Check a string can be parsed as a valid condition. Do a local contextual analysis.*/
    public boolean checkIsValidCondition(String input) throws Exception {

        String[] expression = input.split(" ",3 );
        if(expression.length != 3) //not able to split into Factor Operator Factor
        {
            Exception o = new Exception("Conditions must be of the form 'Factor Operator Factor'");
            throw o;
        }

        if(expression[2].indexOf(' ') != -1)
        {
            Exception e = new Exception("Too many arguments");
            throw e;
        }

        if(isBool(expression[0]))
        {
            if(isInteger(expression[2]))
            {
                Exception e = new Exception("Factor types must match.");
                throw e;
            }

        }

        else if(isInteger(expression[0]))
        {
            if(isBool(expression[2]))
            {
                Exception e = new Exception("Factor types must match.");
                throw e;
            }
        }
        else
        {
            isValidVariable(expression[0]);

        }
        if(!isBool(expression[2]) && !isInteger(expression[2]))
        {
            isValidVariable(expression[2]);
        }



        switch(expression[1])
        {
            case "<":
            case ">":
            case "<=":
            case ">=":

                if(isBool(expression[0]))
                {
                    Exception e = new Exception("Invalid operator");
                    throw e;
                }
                break;

            case "==":

            case "!=":

                break;

            case "&&":

            case "||":

                if(isInteger(expression[0]))
                {
                    Exception e = new Exception("Invalid operator");
                    throw e;
                }
                break;

            default:
                Exception e = new Exception("Invalid operator");
                throw e;


        }
        return true;
    }

    public boolean isInteger(String input)
    {
        try
        {
            int temp = Integer.parseInt(input);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    public boolean isBool(String input)
    {
        if(input.equals("true") || input.equals("false") || input.equals("!false") || input.equals("!true"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /*Take in a program from the editor, format it into a format to be saved. */
    public void getPrintLines(ArrayList<String> output, TreeNode root)
    {
        ArrayList<TreeNode> childs = root.getChildren();

        if(childs.size() != 0)
        {
            if(root.ID != 0)
            {
                output.add(root.data + ";" + Integer.toString(root.type) + ";" + Integer.toString(root.ID) + ";" + Integer.toString(root.getParent().ID));
            }
            for(int x = 0; x < childs.size(); x++)
            {
                getPrintLines(output, childs.get(x));
            }
        }
        else
        {
            if(root.ID != 0)
            {
            output.add(root.data + ";" + Integer.toString(root.type) + ";" + Integer.toString(root.ID) + ";" + Integer.toString(root.getParent().ID));
            }
        }
    }

    /*Load the names of all the saved files. */
    public ArrayList<String> loadFileNamesToArray(Context context)
    {
        ArrayList<String> fileNames = new ArrayList<>();
        //load existing list of file names;
        FileInputStream fis = null;
        try {
            fis = context.openFileInput("program_names.ms");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {
                fileNames.add(text);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileNames;
    }

    /*Save a new file to the list of saved files. */
    public void saveFileNamesWithNewFile( String myProgramName,Context context, ArrayList<String> fileNames)
    {
        FileOutputStream fos = null;
        String lineSeparator = System.getProperty("line.separator");
        //save new list, now with new file name, with it at top of the list
        try {
            fos = context.openFileOutput("program_names.ms", context.MODE_PRIVATE);
            fos.write(myProgramName.getBytes());
            fos.write(lineSeparator.getBytes());
            for(int x = 0; x < fileNames.size(); x++)
            {
                String temp = fileNames.get(x);
                if(!temp.equals(myProgramName))
                {
                    fos.write(temp.getBytes());
                    fos.write(lineSeparator.getBytes());
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (fos != null)
            {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*Load a files code into an array that can be used by M#. */
    public ArrayList<String> loadCodeIntoArrayFromFile(String programName, Context context) {
        ArrayList<String> loadedCode = new ArrayList<>();
        try {
            FileInputStream fis = null;
            fis = context.openFileInput(programName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {
                loadedCode.add(text);
            }

        }
        catch(FileNotFoundException e)
        {

        }
        catch (IOException e) {

            e.printStackTrace();
        }

        finally {

        }

        return loadedCode;

}

    /*Write a program to a file to be retrieved later. Takes in TreeNode */
    public void writeProgramToFile(String myProgramName, Context context, TreeNode root)
    {
        String lineSeparator = System.getProperty("line.separator");
        //save the program
        ArrayList<String> saveLines = new ArrayList<String>();
        getPrintLines(saveLines, root);
        FileOutputStream fos = null;

        try {
            fos = context.openFileOutput(myProgramName, context.MODE_PRIVATE);
            for(int x = 0; x < saveLines.size(); x++)
            {
                fos.write(saveLines.get(x).getBytes());
                fos.write(lineSeparator.getBytes());
            }
            //Toast.makeText(context, "Saved to " + context.getFilesDir() + "/" + myProgramName, Toast.LENGTH_LONG).show();
            //debuging toast

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (fos != null)
            {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*Write a program to a file to be retrieved later. Takes in ArrayList */
    public void writeProgramToFile(String myProgramName, Context context, ArrayList<String> code) //overload that takes arraylist instead of treenode
    {
        String lineSeparator = System.getProperty("line.separator");
        //save the program
        ArrayList<String> saveLines = code;
        FileOutputStream fos = null;

        try {
            fos = context.openFileOutput(myProgramName, context.MODE_PRIVATE);
            for(int x = 0; x < saveLines.size(); x++)
            {
                fos.write(saveLines.get(x).getBytes());
                fos.write(lineSeparator.getBytes());
            }
            //Toast.makeText(context, "Saved to " + context.getFilesDir() + "/" + myProgramName, Toast.LENGTH_LONG).show();
            //debuging toast

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (fos != null)
            {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
