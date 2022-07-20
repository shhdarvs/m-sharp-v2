package com.example.msharp;

/*Integer factor. */
public class IntegerFactor extends Factor {

    int element;

    public IntegerFactor(int element)
    {
        this.element = element;
    }

    @Override
    public String rawInput() {
        return String.valueOf(element);
    }

    @Override
    public void execute()
    {

    }

    @Override
    public int type() {
        return 6;
    }

    @Override
    public int resultInt() {
        return element;
    }

    @Override
    public String resultBool() {
        return null;
    }

    @Override
    public String resultString() {
        return null;
    }
}
