package frontend.util;

import frontend.syntax.CompUnit;
import midend.util.Value;

public class ParserUnit extends Node {
    public State state;

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

    public void buildLeftRecursion(boolean pass) {
        if (pass)
            for (Node node : nodes)
                if (node instanceof ParserUnit)
                    ((ParserUnit) node).buildLeftRecursion(true);
    }

    public void eliminateLeftRecursion(boolean pass) {
        if (pass)
            for (Node node : nodes)
                if (node instanceof ParserUnit)
                    ((ParserUnit) node).eliminateLeftRecursion(true);
    }

    public void semantic() {
        for (Node node : nodes)
            if (node instanceof ParserUnit)
                ((ParserUnit) node).semantic();
    }

    public void setState(State state) {
        this.state = new State(state.getLoopNumber(), state.getIfNumber(), state.isHaveElse(), state.shouldReturnValue(), state.getBlockNumber(), state.getLAndNumber(), state.getLOrNumber());
        for (Node node : nodes)
            if (node instanceof ParserUnit)
                ((ParserUnit) node).setState(this.state);
    }

    public Value generateIR() {
        for (Node node : nodes)
            if (node instanceof ParserUnit)
                ((ParserUnit) node).generateIR();
        return null;
    }
}
