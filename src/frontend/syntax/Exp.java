package frontend.syntax;

import frontend.lexical.LexicalitySupporter;

public class Exp extends ParserUnit {
    Exp() {
        type = "Exp";
    }

    public static Exp parser(LexicalitySupporter lexicalitySupporter) {
        Exp exp = new Exp();
        exp.add(AddExp.parser(lexicalitySupporter));
        return exp;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return AddExp.pretreat(lexicalitySupporter);
    }
}
