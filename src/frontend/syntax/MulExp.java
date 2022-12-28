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

public class MulExp extends ParserUnit {
    MulExp() {
        setType("MulExp");
    }

    public static MulExp parser(LexicalitySupporter lexicalitySupporter) {
        MulExp mulExp = new MulExp();
        mulExp.add(UnaryExp.parser(lexicalitySupporter));
        while (lexicalitySupporter.read().getType().equals("MULT") ||
                lexicalitySupporter.read().getType().equals("DIV") ||
                lexicalitySupporter.read().getType().equals("MOD") ||
                lexicalitySupporter.read().getType().equals("BITAND")) {
            mulExp.add(lexicalitySupporter.readAndNext());
            mulExp.add(UnaryExp.parser(lexicalitySupporter));
        }
        mulExp.buildLeftRecursion(false);
        return mulExp;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return UnaryExp.pretreat(lexicalitySupporter);
    }

    public void eliminateLeftRecursion(boolean pass) {
        ArrayList<Node> nodes = new ArrayList<>();
        MulExp mulExp = this;
        while (mulExp.getNode(0) instanceof MulExp) {
            nodes.add(0, mulExp.getNode(2));
            nodes.add(0, mulExp.getNode(1));
            mulExp = ((MulExp) mulExp.getNode(0));
        }
        nodes.add(0, mulExp.getNode(0));
        this.nodes = nodes;
        if (pass)
            super.eliminateLeftRecursion(true);
    }

    public void buildLeftRecursion(boolean pass) {
        int length = nodes.size() / 2;
        if (!(length == 0 || getNode(0) instanceof MulExp)) {
            MulExp mulExp = new MulExp(), temp;
            mulExp.add(getNode(0));
            for (int i = 0; i < length; i++) {
                temp = new MulExp();
                temp.add(mulExp);
                temp.add(getNode(1 + 2 * i));
                temp.add(getNode(2 * (1 + i)));
                mulExp = temp;
            }
            nodes = mulExp.nodes;
        }
        if (pass)
            super.buildLeftRecursion(true);
    }

    public int getInteger() {
        if (nodes.size() == 1) {
            return ((UnaryExp) getNode(0)).getInteger();
        } else {
            if (getNode(1).getType().equals("MULT")) {
                return ((MulExp) getNode(0)).getInteger() * ((UnaryExp) getNode(2)).getInteger();
            } else if (getNode(1).getType().equals("DIV")) {
                return ((MulExp) getNode(0)).getInteger() / ((UnaryExp) getNode(2)).getInteger();
            } else if (getNode(1).getType().equals("MOD")) {
                return ((MulExp) getNode(0)).getInteger() % ((UnaryExp) getNode(2)).getInteger();
            } else {
                return ((MulExp) getNode(0)).getInteger() & ((UnaryExp) getNode(2)).getInteger();
            }
        }
    }

    public int getDimension() {
        int res = 0;
        for (Node node : nodes) {
            if (node instanceof UnaryExp && ((UnaryExp) node).getDimension() > res) {
                res = ((UnaryExp) node).getDimension();
            } else if (node instanceof MulExp && ((MulExp) node).getDimension() > res) {
                res = ((MulExp) node).getDimension();
            }
        }
        return res;
    }

    @Override
    public Value generateIR() {
        if (nodes.size() == 1)
            return ((UnaryExp) getNode(0)).generateIR();
        Value v1 = ((MulExp) getNode(0)).generateIR();
        Value v2 = ((UnaryExp) getNode(2)).generateIR();
        if (v1.getType() == ValueType.Imm && v2.getType() == ValueType.Imm)
            if (getNode(1).getType().equals("MULT"))
                return new Value(v1.getValue() * v2.getValue());
            else if (getNode(1).getType().equals("DIV"))
                return new Value(v1.getValue() / v2.getValue());
            else if (getNode(1).getType().equals("MOD"))
                return new Value(v1.getValue() % v2.getValue());
            else
                return new Value(v1.getValue() & v2.getValue());
        Value value = Allocator.getInstance().getTemp();
        if (getNode(1).getType().equals("MULT"))
            IRSupporter.getInstance().addIRCode(new BinaryAssign(value, v1, v2, Operator.MULTI));
        else if (getNode(1).getType().equals("DIV"))
            IRSupporter.getInstance().addIRCode(new BinaryAssign(value, v1, v2, Operator.DIV));
        else if (getNode(1).getType().equals("MOD"))
            IRSupporter.getInstance().addIRCode(new BinaryAssign(value, v1, v2, Operator.MOD));
        else
            IRSupporter.getInstance().addIRCode(new BinaryAssign(value, v1, v2, Operator.BITAND));
        return value;
    }
}
