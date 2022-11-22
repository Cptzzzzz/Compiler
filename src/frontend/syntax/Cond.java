package frontend.syntax;

import frontend.util.LexicalitySupporter;
import frontend.util.ParserUnit;

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
