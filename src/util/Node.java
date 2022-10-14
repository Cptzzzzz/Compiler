package util;

import java.util.ArrayList;

public class Node {
    public String type;
    public Node parent;
    public ArrayList<Node> nodes;

    public static boolean shouldOutput(String type) {
        if (type.equals("BType") ||
                type.equals("BlockItem") ||
                type.equals("Decl")) return false;
        return true;
    }

    public void setParent(Node node) {
        this.parent = node;
    }

    public void output() {
        if (nodes != null)
            for (Node node : nodes) {
                node.output();
            }
        if (Node.shouldOutput(getType()))
            OutputWriter.writeln(toString());
    }

    public String getType() {
        return type;
    }

    public void buildParent(Node parent) {
        setParent(parent);
        if (nodes != null)
            for (Node node : nodes) {
                node.buildParent(this);
            }
    }
}
