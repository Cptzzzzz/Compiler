package syntax;

import lexical.LexicalitySupporter;

public class Cond extends ParserUnit {
    Cond() {
        name = "Cond";
    }

    public static Cond parser(LexicalitySupporter lexicalitySupporter) {
        Cond cond = new Cond();
        cond.add(LOrExp.parser(lexicalitySupporter));
        return cond;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (LOrExp.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }
}
