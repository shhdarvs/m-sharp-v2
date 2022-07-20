package com.example.msharp;

public class BoolFactor extends Factor{

    public String myValue;

    /*Bool factor*/
    public BoolFactor(String myValue)
    {
        this.myValue = myValue;
    }

    /*store its raw value. */
    @Override
    public String rawInput() {
        return myValue;
    }

    @Override
    public void execute()
    {
    }

    @Override
    public int type() {
        return 5;
    }

    @Override
    public int resultInt() {
        return 0;
    }


    /*When asked for result, will evaluate if its notted first, then return its result. */
    @Override
    public String resultBool()
    {
        String[] split = myValue.split("!",2);
        if(split[0].equals("")) // notted
        {

            if(myValue.equals("true"))
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
            return myValue;
        }

    }

    @Override
    public String resultString() {
        return null;
    }
}
