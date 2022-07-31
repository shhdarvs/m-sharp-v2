package com.example.msharp.expression;

import com.example.msharp.Logging;
import com.example.msharp.factor.Factor;
import com.example.msharp.factor.IntegerFactor;

/**
 * This class represents an expression that is binary, i.e. has two operands separated by an operator
 */
public class BinaryExpression extends Expression {
    public static final String TAG = "BinaryExpression";

    public String operator;
    public Expression e1, e2;       //left and right operands
    public int type;
    private String result;

    public BinaryExpression(String operator, Expression e1, Expression e2, int type) {
        this.operator = operator;
        this.e1 = e1;
        this.e2 = e2;
        this.type = type;
    }

    public String result() {
        return "";
    }

    private String evaluateOperand(Expression op) {

        String lhs = "";
        //If the LHS operand is a factor --> literal or variable
        if (op instanceof Factor) {
            lhs = ((Factor) op).rawInput();
        }

        //If the LHS operand is a unary expression --> +7 or -6.2
        else if (op instanceof UnaryExpression) {
            Expression unary = ((UnaryExpression) op).e;
            try {
                unary.execute(); //evaluate the unary expression and obtain result
            } catch (Exception e) {
                Logging.error(TAG, e);
            }

            lhs = unary.resultString();
        }

        //If the LHS operand is a binary expression --> 5 + 7 or 6.2 + 3
        else if (op instanceof BinaryExpression) {
            try {
                ((Expression) ((BinaryExpression) op)).execute();
            } catch (Exception e) {
                Logging.error(TAG, e);
            }

            lhs = ((Expression) ((BinaryExpression) op)).resultString();
        }

        return lhs;
    }

    @Override
    public void execute() {
        //We already know the result type of the expression. This makes the evaluation easier
        String lhs = evaluateOperand(e1);
        String rhs = evaluateOperand(e2);


    }


    @Override
    public int type() throws Exception {
        return type;
        //return ExpressionHelper.binExp;
    }

    @Override
    public int resultInt() {
        return 0;
    }

    @Override
    public double resultDouble() {
        return 0;
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
