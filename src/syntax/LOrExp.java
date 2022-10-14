package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;

import java.util.ArrayList;

public class LOrExp extends ParserUnit {
    LOrExp() {
        type = "LOrExp";
    }

    public static LOrExp parser(LexicalitySupporter lexicalitySupporter) {
        LOrExp lOrExp, temp;
        LAndExp lAndExp;
        ArrayList<LAndExp> arrayList = new ArrayList<>();
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        arrayList.add(LAndExp.parser(lexicalitySupporter));
        while (lexicalitySupporter.read().getType().equals("OR")) {
            lexicalities.add(lexicalitySupporter.readAndNext());
            arrayList.add(LAndExp.parser(lexicalitySupporter));
        }
        int length = lexicalities.size();
        lAndExp = arrayList.get(0);
        lOrExp = new LOrExp();
        lOrExp.add(lAndExp);
        for (int i = 0; i < length; i++) {
            temp = new LOrExp();
            temp.add(lOrExp);
            temp.add(lexicalities.get(i));
            temp.add(arrayList.get(i + 1));
            lOrExp = temp;
        }
        return lOrExp;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (LAndExp.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }
}
