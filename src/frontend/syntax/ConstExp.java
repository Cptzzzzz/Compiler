package frontend.syntax;

import frontend.lexical.LexicalitySupporter;

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
        return AddExp.pretreat(lexicalitySupporter);
    }
}
