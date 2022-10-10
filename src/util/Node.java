package util;

import java.util.ArrayList;

public class Node {


    public ArrayList<Node> nodes;
    public boolean isOutput = true;

    public void output() {
        if (nodes != null)
            for (Node node : nodes) {
                node.output();
            }
        if(isOutput)
        OutputWriter.writeln(toString());
    }

    public String getDetail(){
        return nodes.toString();
    }
}
