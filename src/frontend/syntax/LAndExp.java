package frontend.syntax;

import frontend.util.LexicalitySupporter;
import frontend.util.Node;
import frontend.util.ParserUnit;
import frontend.util.State;
import midend.ir.Branch;
import midend.ir.Jump;
import midend.ir.Label;
import midend.util.IRSupporter;
import midend.util.Value;
import midend.util.ValueType;
import util.Allocator;

import java.util.ArrayList;

public class LAndExp extends ParserUnit {
    LAndExp() {
        setType("LAndExp");
    }

    public static LAndExp parser(LexicalitySupporter lexicalitySupporter) {
        LAndExp lAndExp = new LAndExp();
        lAndExp.add(EqExp.parser(lexicalitySupporter));
        while (lexicalitySupporter.read().getType().equals("AND")) {
            lAndExp.add(lexicalitySupporter.readAndNext());
            lAndExp.add(EqExp.parser(lexicalitySupporter));
        }
        lAndExp.buildLeftRecursion(false);
        return lAndExp;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return EqExp.pretreat(lexicalitySupporter);
    }

    public void eliminateLeftRecursion(boolean pass) {
        ArrayList<Node> nodes = new ArrayList<>();
        LAndExp lAndExp = this;
        while (lAndExp.getNode(0) instanceof LAndExp) {
            nodes.add(0, lAndExp.getNode(2));
            nodes.add(0, lAndExp.getNode(1));
            lAndExp = ((LAndExp) lAndExp.getNode(0));
        }
        nodes.add(0, lAndExp.getNode(0));
        this.nodes = nodes;
        if (pass)
            super.eliminateLeftRecursion(true);
    }

    public void buildLeftRecursion(boolean pass) {
        int length = nodes.size() / 2;
        if (!(length == 0 || getNode(0) instanceof LAndExp)) {
            LAndExp lAndExp = new LAndExp(), temp;
            lAndExp.add(getNode(0));
            for (int i = 0; i < length; i++) {
                temp = new LAndExp();
                temp.add(lAndExp);
                temp.add(getNode(1 + 2 * i));
                temp.add(getNode(2 * (1 + i)));
                lAndExp = temp;
            }
            nodes = lAndExp.nodes;
        }
        if (pass)
            super.buildLeftRecursion(true);
    }

    @Override
    public void setState(State state) {
        eliminateLeftRecursion(false);
        this.state = new State(state.getLoopNumber(), state.getIfNumber(), state.isHaveElse(), state.shouldReturnValue(), state.getBlockNumber(), Allocator.getInstance().getLAndNumber(), state.getLOrNumber());
        for (Node node : nodes) {
            if (node instanceof ParserUnit)
                ((ParserUnit) node).setState(this.state);
        }
    }

    @Override
    public Value generateIR() {
        Value value;
        for (Node node : nodes) {
            if (node instanceof EqExp) {
                value = ((EqExp) node).generateIR();
                if (value.getType() == ValueType.Imm) {
                    if (value.getValue() == 0)
                        IRSupporter.getInstance().addIRCode(new Jump(String.format("LAnd_%d_fail", state.getLAndNumber())));
                } else
                    IRSupporter.getInstance().addIRCode(new Branch(value, String.format("LAnd_%d_fail", state.getLAndNumber()), true));
            }
        }
        IRSupporter.getInstance().addIRCode(new Jump(String.format("LOr_%d_success", state.getLOrNumber())));
        IRSupporter.getInstance().addIRCode(new Label(String.format("LAnd_%d_fail", state.getLAndNumber())));
        return null;
    }
}
