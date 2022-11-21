package frontend.syntax;

import frontend.lexical.Lexicality;
import frontend.lexical.LexicalitySupporter;
import util.Node;

import java.util.ArrayList;

public class AddExp extends ParserUnit {
    AddExp() {
        setType("AddExp");
    }

    public static AddExp parser(LexicalitySupporter lexicalitySupporter) {
        AddExp addExp = new AddExp();
        addExp.add(MulExp.parser(lexicalitySupporter));
        while (lexicalitySupporter.read().getType().equals("PLUS") ||
                lexicalitySupporter.read().getType().equals("MINU")) {
            addExp.add(lexicalitySupporter.readAndNext());
            addExp.add(MulExp.parser(lexicalitySupporter));
        }
        addExp.buildLeftRecursion();
        return addExp;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return MulExp.pretreat(lexicalitySupporter);
    }

    public void eliminateLeftRecursion() {
        ArrayList<Node> nodes = new ArrayList<>();
        AddExp addExp = this;
        while (addExp.nodes.get(0) instanceof AddExp) {
            nodes.add(0, addExp.nodes.get(2));
            nodes.add(0, addExp.nodes.get(1));
            addExp = ((AddExp) addExp.nodes.get(0));
        }
        nodes.add(0, addExp.nodes.get(0));
        this.nodes = nodes;
        super.eliminateLeftRecursion();
    }

    public void buildLeftRecursion() {
        int length = nodes.size() / 2;
        if (!(length == 0 || nodes.get(0) instanceof AddExp)) {
            AddExp addExp = new AddExp(), temp;
            addExp.add(nodes.get(0));
            for (int i = 0; i < length; i++) {
                temp = new AddExp();
                temp.add(addExp);
                temp.add(nodes.get(1 + 2 * i));
                temp.add(nodes.get(2 * (1 + i)));
                addExp = temp;
            }
            nodes = addExp.nodes;
        }
        super.buildLeftRecursion();
    }

    public int getInteger() {
        if (nodes.size() == 1) {
            return ((MulExp) nodes.get(0)).getInteger();
        } else {
            if (nodes.get(1).getType().equals("PLUS")) {
                return ((AddExp) nodes.get(0)).getInteger() + ((MulExp) nodes.get(2)).getInteger();
            } else {
                return ((AddExp) nodes.get(0)).getInteger() - ((MulExp) nodes.get(2)).getInteger();
            }
        }
    }

    public int getDimension() {
        int res = 0;
        for (Node node : nodes) {
            if (node instanceof AddExp && ((AddExp) node).getDimension() > res) {
                res = ((AddExp) node).getDimension();
            } else if (node instanceof MulExp && ((MulExp) node).getDimension() > res) {
                res = ((MulExp) node).getDimension();
            }
        }
        return res;
    }
}
