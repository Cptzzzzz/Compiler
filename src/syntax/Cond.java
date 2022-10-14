package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.Node;

public class Cond extends ParserUnit {
    Cond() {
        type = "Cond";
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
