package frontend.syntax;

import frontend.util.LexicalitySupporter;
import frontend.util.ParserUnit;

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
        } else if (LVal.pretreat(lexicalitySupporter))
            primaryExp.add(LVal.parser(lexicalitySupporter));
        else if (Number.pretreat(lexicalitySupporter))
            primaryExp.add(Number.parser(lexicalitySupporter));
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
        if (getNode(0).getType().equals("LPARENT"))
            return ((Exp) getNode(1)).getInteger();
        else if (getNode(0).getType().equals("Number"))
            return ((Number) getNode(0)).getInteger();
        else
            return ((LVal) getNode(0)).getInteger();
    }

    public int getDimension() {
        if (getNode(0).getType().equals("LVal"))
            return ((LVal) getNode(0)).getDimension();
        else if (getNode(0).getType().equals("Number"))
            return 0;
        else
            return ((Exp) getNode(1)).getDimension();
    }
}
