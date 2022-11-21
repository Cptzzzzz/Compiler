package frontend.syntax;

import frontend.lexical.Lexicality;
import frontend.lexical.LexicalitySupporter;
import util.Node;

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
        while (mulExp.nodes.get(0) instanceof MulExp) {
            nodes.add(0, mulExp.nodes.get(2));
            nodes.add(0, mulExp.nodes.get(1));
            mulExp = ((MulExp) mulExp.nodes.get(0));
        }
        nodes.add(0, mulExp.nodes.get(0));
        this.nodes = nodes;
        super.eliminateLeftRecursion();
    }

    public void buildLeftRecursion() {
        int length = nodes.size() / 2;
        if (!(length == 0 || nodes.get(0) instanceof MulExp)) {
            MulExp mulExp = new MulExp(), temp;
            mulExp.add(nodes.get(0));
            for (int i = 0; i < length; i++) {
                temp = new MulExp();
                temp.add(mulExp);
                temp.add(nodes.get(1 + 2 * i));
                temp.add(nodes.get(2 * (1 + i)));
                mulExp = temp;
            }
            nodes = mulExp.nodes;
        }
        super.buildLeftRecursion();
    }

    public int getInteger() {
        if (nodes.size() == 1) {
            return ((UnaryExp) nodes.get(0)).getInteger();
        } else {
            if (nodes.get(1).getType().equals("MULT")) {
                return ((MulExp) nodes.get(0)).getInteger() * ((UnaryExp) nodes.get(2)).getInteger();
            } else if (nodes.get(1).getType().equals("DIV")) {
                return ((MulExp) nodes.get(0)).getInteger() / ((UnaryExp) nodes.get(2)).getInteger();
            } else {
                return ((MulExp) nodes.get(0)).getInteger() % ((UnaryExp) nodes.get(2)).getInteger();
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
