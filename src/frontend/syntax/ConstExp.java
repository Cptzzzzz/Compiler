package frontend.syntax;

import frontend.util.LexicalitySupporter;
import frontend.util.ParserUnit;

public class ConstExp extends ParserUnit {
    ConstExp() {
        setType("ConstExp");
    }

    public static ConstExp parser(LexicalitySupporter lexicalitySupporter) {
        ConstExp constExp = new ConstExp();
        constExp.add(AddExp.parser(lexicalitySupporter));
        return constExp;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return AddExp.pretreat(lexicalitySupporter);
    }

    public int getInteger() {
        return ((AddExp) getNode(0)).getInteger();
    }
}
