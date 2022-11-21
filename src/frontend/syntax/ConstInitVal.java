package frontend.syntax;

import frontend.lexical.LexicalitySupporter;
import util.Node;

import java.util.ArrayList;

public class ConstInitVal extends ParserUnit {
    ConstInitVal() {
        setType("ConstInitVal");
    }

    public static ConstInitVal parser(LexicalitySupporter lexicalitySupporter) {
        ConstInitVal constInitVal = new ConstInitVal();
        if (ConstExp.pretreat(lexicalitySupporter)) {
            constInitVal.add(ConstExp.parser(lexicalitySupporter));
        } else if (lexicalitySupporter.read().getType().equals("LBRACE")) {
            constInitVal.add(lexicalitySupporter.readAndNext());
            if (ConstInitVal.pretreat(lexicalitySupporter)) {
                constInitVal.add(ConstInitVal.parser(lexicalitySupporter));
                while (lexicalitySupporter.read().getType().equals("COMMA")) {
                    constInitVal.add(lexicalitySupporter.readAndNext());
                    constInitVal.add(ConstInitVal.parser(lexicalitySupporter));
                }
            }
            constInitVal.add(lexicalitySupporter.readAndNext());
        }
        return constInitVal;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (ConstExp.pretreat(lexicalitySupporter)) {
            return true;
        }
        return lexicalitySupporter.read().getType().equals("LBRACE");
    }

    public ArrayList<Integer> getIntegers() {
        ArrayList<Integer> res = new ArrayList<>();
        for (Node node : nodes) {
            if (node instanceof ConstInitVal)
                res.addAll(((ConstInitVal) node).getIntegers());
            else if (node instanceof ConstExp)
                res.add(((ConstExp) node).getInteger());
        }
        return res;
    }
}
