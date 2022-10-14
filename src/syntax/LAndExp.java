package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;

import java.util.ArrayList;

public class LAndExp extends ParserUnit {
    LAndExp() {
        type = "LAndExp";
    }

    public static LAndExp parser(LexicalitySupporter lexicalitySupporter) {
        LAndExp lAndExp, temp;
        EqExp eqExp;
        ArrayList<EqExp> arrayList = new ArrayList<>();
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        arrayList.add(EqExp.parser(lexicalitySupporter));
        while (lexicalitySupporter.read().getType().equals("AND")) {
            lexicalities.add(lexicalitySupporter.readAndNext());
            arrayList.add(EqExp.parser(lexicalitySupporter));
        }
        int length = lexicalities.size();
        eqExp = arrayList.get(0);
        lAndExp = new LAndExp();
        lAndExp.add(eqExp);
        for (int i = 0; i < length; i++) {
            temp = new LAndExp();
            temp.add(lAndExp);
            temp.add(lexicalities.get(i));
            temp.add(arrayList.get(i + 1));
            lAndExp = temp;
        }
        return lAndExp;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (EqExp.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }
}
