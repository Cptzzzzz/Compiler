package frontend.syntax;

import frontend.lexical.LexicalitySupporter;

public class PrimaryExp extends ParserUnit {
    PrimaryExp() {
        type = "PrimaryExp";
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
}
