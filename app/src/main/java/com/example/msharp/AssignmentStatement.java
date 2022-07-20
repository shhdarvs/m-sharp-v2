package com.example.msharp;

import java.io.File;
import java.util.Map;
// i think im happy, provided expression works as intended.
public class AssignmentStatement extends Statement
{
    public String variable;
    public Expression expression;
    public Map<String,Integer> Numbers;
    public Map<String, String> Strings;
    public Map<String, String> Bools;

    /*Statement that assigns a value to a variable. */
    public AssignmentStatement(String variable, Expression expression, Map<String,Integer> Numbers, Map<String, String> Strings, Map<String, String> Bools)
    {
        this.variable = variable;
        this.expression = expression;
        this.Strings = Strings;
        this.Bools = Bools;
        this.Numbers = Numbers;
    }

    /*Remove a variable from all memory maps. */
    public void removeVariable()
    {
        if(Numbers.containsKey(variable))
        {
            Numbers.remove(variable);
        }
        if(Bools.containsKey(variable))
        {
            Bools.remove(variable);
        }
        if(Strings.containsKey(variable))
        {
            Strings.remove(variable);
        }
    }


    /*Assign a value to a variable. */
    @Override
    public void execute() throws Exception {
        Functions fun = new Functions();
        /*
        type 0 = addExp
        type 1 = andExp
        type 2 = eqlExp
        type 3 = multExp
        type 4 = relExp
        type 5 = boolFact
        type 6 = intFact
        type 7 = stringFact
        type 8 = varFact
        */
        /*Evaluate result of the expression. */
        expression.execute();

        /*Switch based on expression type. */
        switch (expression.type())
        {
            case 0:
            case 3:
            case 6:
                removeVariable();
                Numbers.put(variable, expression.resultInt()); //Its a number.
                break;
            case 1:
            case 2:
            case 4:
            case 5:
                removeVariable();
                Bools.put(variable, expression.resultBool()); //Its a bool
                break;
            case 7:
                removeVariable();
                Strings.put(variable, expression.resultString()); //its a string
                break;
            default:
                Exception e = new Exception("Attempting to access undeclared variable");
                throw e;

        }

    }
}
