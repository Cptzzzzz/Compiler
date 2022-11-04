package syntax;

import lexical.LexicalitySupporter;

public class ConstExp extends ParserUnit {
    ConstExp() {
        type = "ConstExp";
    }

    public static ConstExp parser(LexicalitySupporter lexicalitySupporter) {
        ConstExp constExp = new ConstExp();
        constExp.add(AddExp.parser(lexicalitySupporter));
        return constExp;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (AddExp.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }

    public int getValue() {
        return ((AddExp) nodes.get(0)).getValue();
    }

    public String generateIntermediateCode() {
        return null;
    }
}
