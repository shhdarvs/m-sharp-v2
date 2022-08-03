package com.prod.msharp.analysis.syntax;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.msharp.Logging;
import com.prod.msharp.analysis.text.TextSpan;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class AST {
    public final String TAG = getClass().getSimpleName();

    public abstract TokenKind kind();

    //public abstract List<AST> getChildren();

    public TextSpan getSpan() {
        List<AST> children = getChildren();

        return TextSpan.createFromBounds(
                children.get(0).getSpan().start,
                children.get(children.size() - 1).getSpan().end);
    }

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void write(Writer writer) {
        prettyPrint(writer, this, "", true);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void prettyPrint(Writer writer, AST node, String indent, boolean isLast) {
        // └──
        // ├──
        // │

        String marker = isLast ? "└──" : "├──";

        try {
            writer.write(indent);
            writer.write(marker);
            writer.write(node.kind().name());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (node instanceof Token) {
            Token t = (Token) node;

            if (t.value != null) {
                try {
                    writer.write(" ");
                    writer.write(String.valueOf(t.value));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        try {
            writer.write(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }

        indent += isLast ? "    " : "│   ";

        AST lastChild = null;

        if (node.getChildren().size() > 0)
            lastChild = node.getChildren().get(node.getChildren().size() - 1);

        for (AST child : node.getChildren())
            prettyPrint(writer, child, indent, child == lastChild);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    @Override
    public String toString() {
        var writer = new StringWriter();

        write(writer);
        return writer.toString();
    }
}
