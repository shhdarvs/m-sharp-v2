package com.example.msharp.statement;

import com.example.msharp.AST;

/**
 * The Statement class inherits from the AST class. All statement will inherit from this class and implement the method execute().
 */
public abstract class Statement extends AST {

    public abstract void execute();
}
