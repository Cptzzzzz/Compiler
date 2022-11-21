package frontend.syntax;

import frontend.lexical.LexicalitySupporter;
import util.Node;

import java.util.ArrayList;

public class ParserUnit extends Node {
    String type;

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
        return String.format("<%s>", getType());
    }

    public String getType() {
        return type;
    }

    public void add(Node node) {
        if (nodes == null) {
            nodes = new ArrayList<Node>();
        }
        nodes.add(node);
    }

    public void buildLeftRecursion() {
        for (Node node : nodes) {
            if (node instanceof ParserUnit)
                ((ParserUnit) node).buildLeftRecursion();
        }
    }
    public void eliminateLeftRecursion() {
        for (Node node : nodes) {
            if (node instanceof ParserUnit)
                ((ParserUnit) node).eliminateLeftRecursion();
        }
    }
}
