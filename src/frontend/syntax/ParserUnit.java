package frontend.syntax;

import frontend.lexical.LexicalitySupporter;
import util.Node;

import java.util.ArrayList;

public class ParserUnit extends Node {
    State state;

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

    public void add(Node node) {
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

    public void semantic() {
        for (Node node : nodes) {
            if (node instanceof ParserUnit)
                ((ParserUnit) node).semantic();
        }
    }

    public void setState(State state) {
        this.state = new State(state.getLoopNumber(), state.getIfNumber(), state.isHaveElse(), state.shouldReturnValue());
        for (Node node : nodes) {
            if (node instanceof ParserUnit)
                ((ParserUnit) node).setState(this.state);
        }
    }
}
