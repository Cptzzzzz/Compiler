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

public class RelExp extends ParserUnit {
    RelExp() {
        setType("RelExp");
    }

    public static RelExp parser(LexicalitySupporter lexicalitySupporter) {
        RelExp relExp = new RelExp();
        relExp.add(AddExp.parser(lexicalitySupporter));
        while (lexicalitySupporter.read().getType().equals("LSS") ||
                lexicalitySupporter.read().getType().equals("LEQ") ||
                lexicalitySupporter.read().getType().equals("GRE") ||
                lexicalitySupporter.read().getType().equals("GEQ")) {
            relExp.add(lexicalitySupporter.readAndNext());
            relExp.add(AddExp.parser(lexicalitySupporter));
        }
        relExp.buildLeftRecursion(false);
        return relExp;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return AddExp.pretreat(lexicalitySupporter);
    }

    public void eliminateLeftRecursion(boolean pass) {
        ArrayList<Node> nodes = new ArrayList<>();
        RelExp relExp = this;
        while (relExp.getNode(0) instanceof RelExp) {
            nodes.add(0, relExp.getNode(2));
            nodes.add(0, relExp.getNode(1));
            relExp = ((RelExp) relExp.getNode(0));
        }
        nodes.add(0, relExp.getNode(0));
        this.nodes = nodes;
        if (pass)
            super.eliminateLeftRecursion(true);
    }

    public void buildLeftRecursion(boolean pass) {
        int length = nodes.size() / 2;
        if (!(length == 0 || getNode(0) instanceof RelExp)) {
            RelExp relExp = new RelExp(), temp;
            relExp.add(getNode(0));
            for (int i = 0; i < length; i++) {
                temp = new RelExp();
                temp.add(relExp);
                temp.add(getNode(1 + 2 * i));
                temp.add(getNode(2 * (1 + i)));
                relExp = temp;
            }
            nodes = relExp.nodes;
        }
        if (pass)
            super.buildLeftRecursion(true);
    }

    @Override
    public Value generateIR() {
        if (nodes.size() == 1) {
            return ((ParserUnit) getNode(0)).generateIR();
        } else {
            Value left = ((ParserUnit) getNode(0)).generateIR();
            Value right = ((ParserUnit) getNode(2)).generateIR();
            if (left.getType() == ValueType.Imm && right.getType() == ValueType.Imm) {
                switch (getNode(1).getType()) {
                    case "LSS":
                        return new Value(left.getValue() < right.getValue() ? 1 : 0);
                    case "LEQ":
                        return new Value(left.getValue() <= right.getValue() ? 1 : 0);
                    case "GRE":
                        return new Value(left.getValue() > right.getValue() ? 1 : 0);
                    default:
                        return new Value(left.getValue() >= right.getValue() ? 1 : 0);
                }
            }
            Value value = Allocator.getInstance().getTemp();
            if (getNode(1).getType().equals("LSS"))
                IRSupporter.getInstance().addIRCode(new BinaryAssign(value, left, right, Operator.LSS));
            else if (getNode(1).getType().equals("LEQ"))
                IRSupporter.getInstance().addIRCode(new BinaryAssign(value, left, right, Operator.LEQ));
            else if (getNode(1).getType().equals("GRE"))
                IRSupporter.getInstance().addIRCode(new BinaryAssign(value, left, right, Operator.GRE));
            else if (getNode(1).getType().equals("GEQ"))
                IRSupporter.getInstance().addIRCode(new BinaryAssign(value, left, right, Operator.GEQ));
            return value;
        }
    }
}
