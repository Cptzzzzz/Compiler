package frontend.syntax;

import frontend.lexical.LexicalitySupporter;

public class Cond extends ParserUnit {
    Cond() {
        setType("Cond");
    }

    public static Cond parser(LexicalitySupporter lexicalitySupporter) {
        Cond cond = new Cond();
        cond.add(LOrExp.parser(lexicalitySupporter));
        return cond;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return LOrExp.pretreat(lexicalitySupporter);
    }
}
