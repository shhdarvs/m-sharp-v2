package com.prod.msharp.analysis.syntax;

import com.example.msharp.Logging;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class AST {
    public final String TAG = getClass().getSimpleName();

    public abstract TokenKind kind();

    //public abstract List<AST> getChildren();

    public List<AST> getChildren() {
        List<AST> childrenList = new ArrayList<>();

        var properties = getClass().getDeclaredFields();

        for (Field f : properties) {
            if (AST.class.isAssignableFrom(f.getType())) {
                try {
                    childrenList.add((AST) f.get(this));
                } catch (IllegalAccessException e) {
                    Logging.error(TAG, e);
                }
            }
        }
        return childrenList;
    }
}
