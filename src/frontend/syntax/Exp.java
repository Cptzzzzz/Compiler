package frontend.syntax;

import frontend.util.LexicalitySupporter;
import frontend.util.ParserUnit;
import midend.util.Value;

public class Exp extends ParserUnit {
    Exp() {
        setType("Exp");
    }

    public static Exp parser(LexicalitySupporter lexicalitySupporter) {
        Exp exp = new Exp();
        exp.add(AddExp.parser(lexicalitySupporter));
        return exp;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return AddExp.pretreat(lexicalitySupporter);
    }

    public int getInteger() {
        return ((AddExp) getNode(0)).getInteger();
    }

    public int getDimension() {
        return ((AddExp) getNode(0)).getDimension();
    }

    @Override
    public Value generateIR() {
        return ((AddExp) getNode(0)).generateIR();
    }
}
