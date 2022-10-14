package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;

import java.util.ArrayList;

public class RelExp extends ParserUnit {
    RelExp() {
        type = "RelExp";
    }

    public static RelExp parser(LexicalitySupporter lexicalitySupporter) {
        RelExp relExp,temp;
        AddExp addExp;
        ArrayList<AddExp> arrayList = new ArrayList<>();
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        arrayList.add(AddExp.parser(lexicalitySupporter));
        while (lexicalitySupporter.read().getType().equals("LSS") ||
                lexicalitySupporter.read().getType().equals("LEQ") ||
                lexicalitySupporter.read().getType().equals("GRE") ||
                lexicalitySupporter.read().getType().equals("GEQ")) {
            lexicalities.add(lexicalitySupporter.readAndNext());
                arrayList.add(AddExp.parser(lexicalitySupporter));
        }
        int length = lexicalities.size();
        addExp = arrayList.get(0);
        relExp = new RelExp();
        relExp.add(addExp);
        for (int i = 0; i < length; i++) {
            temp = new RelExp();
            temp.add(relExp);
            temp.add(lexicalities.get(i));
            temp.add(arrayList.get(i + 1));
            relExp = temp;
        }
        return relExp;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (AddExp.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }
}
