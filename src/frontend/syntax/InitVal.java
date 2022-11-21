package frontend.syntax;

import frontend.lexical.LexicalitySupporter;
import util.Node;

import java.util.ArrayList;

public class InitVal extends ParserUnit {
    InitVal() {
        setType("InitVal");
    }

    public static InitVal parser(LexicalitySupporter lexicalitySupporter) {
        InitVal initVal = new InitVal();
        if (Exp.pretreat(lexicalitySupporter)) {
            initVal.add(Exp.parser(lexicalitySupporter));
        } else if (lexicalitySupporter.read().getType().equals("LBRACE")) {
            initVal.add(lexicalitySupporter.readAndNext());
            if (InitVal.pretreat(lexicalitySupporter)) {
                initVal.add(InitVal.parser(lexicalitySupporter));
                while (lexicalitySupporter.read().getType().equals("COMMA")) {
                    initVal.add(lexicalitySupporter.readAndNext());
                    initVal.add(InitVal.parser(lexicalitySupporter));
                }
            }
            initVal.add(lexicalitySupporter.readAndNext());
        }
        return initVal;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (Exp.pretreat(lexicalitySupporter)) {
            return true;
        }
        return lexicalitySupporter.read().getType().equals("LBRACE");
    }

    public ArrayList<Integer> getIntegers() {
        ArrayList<Integer> res = new ArrayList<>();
        for (Node node : nodes) {
            if (node instanceof InitVal)
                res.addAll(((InitVal) node).getIntegers());
            else if (node instanceof Exp)
                res.add(((Exp) node).getInteger());
        }
        return res;
    }
}
