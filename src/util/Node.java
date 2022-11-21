package util;

import frontend.lexical.Lexicality;

import java.util.ArrayList;

public class Node {


    public ArrayList<Node> nodes;
    private boolean isOutput = true;

    private String type;

    public void output() {
        if (!CompilerMode.getInstance().isSyntax())
            return;
        if (nodes != null)
            for (Node node : nodes) {
                node.output();
            }
        if (isOutput)
            OutputWriter.writeln(toString());
    }

    public void setOutput(boolean output) {
        isOutput = output;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        if (this instanceof Lexicality) {
            return this.getContent();
        }
        return null;
    }

    public int getLineNumber() {
        if (this instanceof Lexicality) {
            return this.getLineNumber();
        }
        return 0;
    }
}
