package frontend.syntax;

import frontend.util.LexicalitySupporter;
import frontend.util.Node;
import frontend.util.ParserUnit;
import frontend.util.State;
import midend.ir.Jump;
import midend.ir.Label;
import midend.util.IRSupporter;
import midend.util.Value;
import util.Allocator;

import java.util.ArrayList;

public class LOrExp extends ParserUnit {
    LOrExp() {
        setType("LOrExp");
    }

    public static LOrExp parser(LexicalitySupporter lexicalitySupporter) {
        LOrExp lOrExp = new LOrExp();
        lOrExp.add(LAndExp.parser(lexicalitySupporter));
        while (lexicalitySupporter.read().getType().equals("OR")) {
            lOrExp.add(lexicalitySupporter.readAndNext());
            lOrExp.add(LAndExp.parser(lexicalitySupporter));
        }
        lOrExp.buildLeftRecursion(false);
        return lOrExp;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return LAndExp.pretreat(lexicalitySupporter);
    }

    public void eliminateLeftRecursion(boolean pass) {
        ArrayList<Node> nodes = new ArrayList<>();
        LOrExp lOrExp = this;
        while (lOrExp.getNode(0) instanceof LOrExp) {
            nodes.add(0, lOrExp.getNode(2));
            nodes.add(0, lOrExp.getNode(1));
            lOrExp = ((LOrExp) lOrExp.getNode(0));
        }
        nodes.add(0, lOrExp.getNode(0));
        this.nodes = nodes;
        if (pass)
            super.eliminateLeftRecursion(true);
    }

    public void buildLeftRecursion(boolean pass) {
        int length = nodes.size() / 2;
        if (!(length == 0 || getNode(0) instanceof LOrExp)) {
            LOrExp lOrExp = new LOrExp(), temp;
            lOrExp.add(getNode(0));
            for (int i = 0; i < length; i++) {
                temp = new LOrExp();
                temp.add(lOrExp);
                temp.add(getNode(1 + 2 * i));
                temp.add(getNode(2 * (1 + i)));
                lOrExp = temp;
            }
            nodes = lOrExp.nodes;
        }
        if (pass)
            super.buildLeftRecursion(true);
    }

    @Override
    public void setState(State state) {
        this.state = new State(state.getLoopNumber(), state.getIfNumber(), state.isHaveElse(), state.shouldReturnValue(), state.getBlockNumber(), state.getLAndNumber(), Allocator.getInstance().getLOrNumber());
        for (Node node : nodes) {
            if (node instanceof ParserUnit)
                ((ParserUnit) node).setState(this.state);
        }
    }

    @Override
    public Value generateIR() {
        eliminateLeftRecursion(false);
        for (Node node : nodes)
            if (node instanceof ParserUnit)
                ((ParserUnit) node).generateIR();
        if (state.getLoopNumber() != 0)
            IRSupporter.getInstance().addIRCode(new Jump(String.format("while_%d_end", state.getLoopNumber())));
        else if (!state.isHaveElse())
            IRSupporter.getInstance().addIRCode(new Jump(String.format("if_%d_end", state.getIfNumber())));
        else
            IRSupporter.getInstance().addIRCode(new Jump(String.format("else_%d_start", state.getIfNumber())));
        IRSupporter.getInstance().addIRCode(new Label(String.format("LOr_%d_success", state.getLOrNumber())));
        return null;
    }
}
