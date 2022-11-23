package frontend.syntax;

import frontend.util.LexicalitySupporter;
import frontend.util.Node;
import frontend.util.ParserUnit;
import midend.ir.BinaryAssign;
import midend.util.IRSupporter;
import midend.util.Operator;
import midend.util.Value;
import midend.util.ValueType;
import util.Allocator;

import java.util.ArrayList;

public class EqExp extends ParserUnit {
    EqExp() {
        setType("EqExp");
    }

    public static EqExp parser(LexicalitySupporter lexicalitySupporter) {
        EqExp eqExp = new EqExp();
        eqExp.add(RelExp.parser(lexicalitySupporter));
        while (lexicalitySupporter.read().getType().equals("EQL") ||
                lexicalitySupporter.read().getType().equals("NEQ")) {
            eqExp.add(lexicalitySupporter.readAndNext());
            eqExp.add(RelExp.parser(lexicalitySupporter));
        }
        eqExp.buildLeftRecursion(false);
        return eqExp;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return RelExp.pretreat(lexicalitySupporter);
    }

    public void eliminateLeftRecursion(boolean pass) {
        ArrayList<Node> nodes = new ArrayList<>();
        EqExp eqExp = this;
        while (eqExp.getNode(0) instanceof EqExp) {
            nodes.add(0, eqExp.getNode(2));
            nodes.add(0, eqExp.getNode(1));
            eqExp = ((EqExp) eqExp.getNode(0));
        }
        nodes.add(0, eqExp.getNode(0));
        this.nodes = nodes;
        if (pass)
            super.eliminateLeftRecursion(true);
    }

    public void buildLeftRecursion(boolean pass) {
        int length = nodes.size() / 2;
        if (!(length == 0 || getNode(0) instanceof EqExp)) {
            EqExp eqExp = new EqExp(), temp;
            eqExp.add(getNode(0));
            for (int i = 0; i < length; i++) {
                temp = new EqExp();
                temp.add(eqExp);
                temp.add(getNode(1 + 2 * i));
                temp.add(getNode(2 * (1 + i)));
                eqExp = temp;
            }
            nodes = eqExp.nodes;
        }
        if (pass)
            super.buildLeftRecursion(true);
    }

    @Override
    public Value generateIR() {
        if (nodes.size() == 1)
            return ((RelExp) getNode(0)).generateIR();
        Value v1 = ((EqExp) getNode(0)).generateIR();
        Value v2 = ((RelExp) getNode(2)).generateIR();
        if (v1.getType() == ValueType.Imm && v2.getType() == ValueType.Imm)
            if (getNode(1).getType().equals("EQL"))
                return new Value(v1.getValue() == v2.getValue() ? 1 : 0);
            else
                return new Value(v1.getValue() != v2.getValue() ? 1 : 0);
        Value value = Allocator.getInstance().getTemp();
        if (getNode(1).getType().equals("EQL"))
            IRSupporter.getInstance().addIRCode(new BinaryAssign(value, v1, v2, Operator.EQL));
        else
            IRSupporter.getInstance().addIRCode(new BinaryAssign(value, v1, v2, Operator.NEQ));
        return value;
    }
}
