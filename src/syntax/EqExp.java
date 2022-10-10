package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;

import java.util.ArrayList;

public class EqExp extends ParserUnit {
    EqExp() {
        name = "EqExp";
    }

    public static EqExp parser(LexicalitySupporter lexicalitySupporter) {
        EqExp eqExp, temp;
        RelExp relExp;
        ArrayList<RelExp> arrayList = new ArrayList<>();
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        arrayList.add(RelExp.parser(lexicalitySupporter));
        while (lexicalitySupporter.read().getType().equals("EQL") ||
                lexicalitySupporter.read().getType().equals("NEQ")) {
            lexicalities.add(lexicalitySupporter.readAndNext());
            arrayList.add(RelExp.parser(lexicalitySupporter));
        }
        int length = lexicalities.size();
        relExp = arrayList.get(0);
        eqExp = new EqExp();
        eqExp.add(relExp);
        for (int i = 0; i < length; i++) {
            temp = new EqExp();
            temp.add(eqExp);
            temp.add(lexicalities.get(i));
            temp.add(arrayList.get(i + 1));
            eqExp = temp;
        }
        return eqExp;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (RelExp.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }
}
