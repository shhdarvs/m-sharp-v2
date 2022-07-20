package com.example.msharp;
//import javax.xml.bind.annotation.XmlAttachmentRef;
import java.util.Map;

/*Represents a variable of various types. */
public class VariableFactor extends Factor {

    public String variableName;
    Map<String,Integer> Numbers;
    Map<String, String> Strings;
    Map<String, String> Bools;
    public int type;

    public VariableFactor(String variableName, Map<String,Integer> Numbers, Map<String, String> Strings, Map<String, String> Bools)
    {
        this.variableName = variableName;
        this.Bools = Bools;
        this.Numbers = Numbers;
        this.Strings = Strings;

    }

    /*Get the variable name. */
    @Override
    public String rawInput() {
        return variableName;
    }

    /*Figure out the type of the variable. */
    @Override
    public void execute() throws Exception {
        this.type = type();
    }

    /*Figure out the type of the variable, to then be able to return. */
    @Override
    public int type() throws Exception {
        if(Numbers.containsKey(variableName)) //its an int
        {
            return 3;
        }
        String[] split = variableName.split("!",2); //see if its a notted bool
        if(split[0].equals("")) // notted
        {
            if(Bools.containsKey(split[1]))
            {
                return 4;
            }
        }
        if(Bools.containsKey(variableName)) //see if its a bool
        {
            return 4;
        }

        if(Strings.containsKey(variableName)) // see if its a key
        {
            return 7;
        }
        else
        {
            Exception e = new Exception("Undeclared variable"); //undeclared.
            throw e;
        }

    }

    /*These results are called by other methods based on the type of the var. if the var is of type int, it will request resultInt. etc. */
    @Override
    public int resultInt() {

        return Numbers.get(variableName);
    }

    @Override
    public String resultBool() {
        String[] split = variableName.split("!",2);
        if(split[0].equals("")) // notted
        {
            String temp = Bools.get(split[1]);
            if (temp.equals("true"))
            {
                return "false";

            }
            else
            {
                return "true";
            }
        }
        else
        {
            return Bools.get(variableName);
        }

    }

    @Override
    public String resultString() {
        return Strings.get(variableName);
    }
}
