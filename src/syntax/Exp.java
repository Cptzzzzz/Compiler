package syntax;

import lexical.LexicalitySupporter;

import java.util.ArrayList;

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
        if (AddExp.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }

    public int getDimension() {
        ArrayList<LVal> lVals = this.getLVal();
        int res = 0;
        for (LVal lVal : lVals) {
            if (lVal.getDimension() > res) {
                res = lVal.getDimension();
            }
        }
        return res;
    }
}
