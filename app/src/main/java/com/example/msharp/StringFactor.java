package com.example.msharp;

/*String factor*/
public class StringFactor extends Factor
{
    String element;
    public StringFactor(String element)
    {
        this.element = element;
    }

    @Override
    public void execute()
    {

    }

    @Override
    public int type() {
        return 7;
    }

    @Override
    public int resultInt() {
        return 0;
    }

    @Override
    public String resultBool() {
        return null;
    }

    @Override
    public String resultString() {
        return element;
    }

    @Override
    public String rawInput() {
        return null;
    }
}
