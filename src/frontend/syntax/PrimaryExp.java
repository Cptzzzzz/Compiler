package frontend.syntax;

import frontend.lexical.LexicalitySupporter;

public class PrimaryExp extends ParserUnit {
    PrimaryExp() {
        setType("PrimaryExp");
    }

    public static PrimaryExp parser(LexicalitySupporter lexicalitySupporter) {
        PrimaryExp primaryExp = new PrimaryExp();
        if (lexicalitySupporter.read().getType().equals("LPARENT")) {
            primaryExp.add(lexicalitySupporter.readAndNext());
            primaryExp.add(Exp.parser(lexicalitySupporter));
            primaryExp.add(lexicalitySupporter.readAndNext());
        } else if (LVal.pretreat(lexicalitySupporter)) {
            primaryExp.add(LVal.parser(lexicalitySupporter));
        } else if (Number.pretreat(lexicalitySupporter)) {
            primaryExp.add(Number.parser(lexicalitySupporter));
        }
        return primaryExp;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (lexicalitySupporter.read().getType().equals("LPARENT")) {
            return true;
        } else if (LVal.pretreat(lexicalitySupporter)) {
            return true;
        } else return Number.pretreat(lexicalitySupporter);
    }

    public int getInteger() {
        if (nodes.get(0).getType().equals("LPARENT")) {
            return ((Exp) nodes.get(1)).getInteger();
        } else if (nodes.get(0).getType().equals("Number")) {
            return ((Number) nodes.get(0)).getInteger();
        } else {
            return ((LVal) nodes.get(0)).getInteger();
        }
    }

    public int getDimension() {
        if (nodes.get(0).getType().equals("LVal")) {
            return ((LVal) nodes.get(0)).getDimension();
        } else if (nodes.get(0).getType().equals("Number")) {
            return 0;
        } else {
            return ((Exp) nodes.get(1)).getDimension();
        }
    }
}
