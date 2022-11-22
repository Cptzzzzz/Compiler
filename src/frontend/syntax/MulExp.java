package frontend.syntax;

import frontend.util.LexicalitySupporter;
import frontend.util.Node;
import frontend.util.ParserUnit;

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
                lexicalitySupporter.read().getType().equals("MOD")) {
            mulExp.add(lexicalitySupporter.readAndNext());
            mulExp.add(UnaryExp.parser(lexicalitySupporter));
        }
        mulExp.buildLeftRecursion();
        return mulExp;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return UnaryExp.pretreat(lexicalitySupporter);
    }

    public void eliminateLeftRecursion() {
        ArrayList<Node> nodes = new ArrayList<>();
        MulExp mulExp = this;
        while (mulExp.getNode(0) instanceof MulExp) {
            nodes.add(0, mulExp.getNode(2));
            nodes.add(0, mulExp.getNode(1));
            mulExp = ((MulExp) mulExp.getNode(0));
        }
        nodes.add(0, mulExp.getNode(0));
        this.nodes = nodes;
        super.eliminateLeftRecursion();
    }

    public void buildLeftRecursion() {
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
        super.buildLeftRecursion();
    }

    public int getInteger() {
        if (nodes.size() == 1) {
            return ((UnaryExp) getNode(0)).getInteger();
        } else {
            if (getNode(1).getType().equals("MULT")) {
                return ((MulExp) getNode(0)).getInteger() * ((UnaryExp) getNode(2)).getInteger();
            } else if (getNode(1).getType().equals("DIV")) {
                return ((MulExp) getNode(0)).getInteger() / ((UnaryExp) getNode(2)).getInteger();
            } else {
                return ((MulExp) getNode(0)).getInteger() % ((UnaryExp) getNode(2)).getInteger();
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
}
