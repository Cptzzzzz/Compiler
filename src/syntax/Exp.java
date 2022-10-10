package syntax;

import lexical.LexicalitySupporter;

public class Exp extends ParserUnit {
    Exp() {
        name = "Exp";
    }

    public static Exp parser(LexicalitySupporter lexicalitySupporter) {
        Exp exp = new Exp();
        exp.add(AddExp.parser(lexicalitySupporter));
        return exp;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (AddExp.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }
}
