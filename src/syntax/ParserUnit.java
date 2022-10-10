package syntax;

import lexical.LexicalitySupporter;
import util.Node;

import java.util.ArrayList;

public class ParserUnit extends Node {
    String name;

    public ParserUnit() {
    }

    public static CompUnit treeBuilder() {
        LexicalitySupporter lexicalitySupporter = new LexicalitySupporter();
        return CompUnit.parser(lexicalitySupporter);
    }

    public static ParserUnit parser(LexicalitySupporter lexicalitySupporter) {
        return new ParserUnit();
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return true;
    }

    public String toString() {
        return String.format("<%s>", getName());
    }

    public String getName() {
        return name;
    }

    public void add(Node node) {
        if (nodes == null) {
            nodes = new ArrayList<Node>();
        }
        nodes.add(node);
    }


}
