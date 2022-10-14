package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;

import java.util.ArrayList;

public class AddExp extends ParserUnit {
    AddExp() {
        type = "AddExp";
    }

    public static AddExp parser(LexicalitySupporter lexicalitySupporter) {
        AddExp addExp, temp;
        MulExp mulExp;
        ArrayList<MulExp> arrayList = new ArrayList<>();
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        arrayList.add(MulExp.parser(lexicalitySupporter));
        while (lexicalitySupporter.read().getType().equals("PLUS") ||
                lexicalitySupporter.read().getType().equals("MINU")) {
            lexicalities.add(lexicalitySupporter.readAndNext());
            arrayList.add(MulExp.parser(lexicalitySupporter));
        }
        int length = lexicalities.size();
        mulExp = arrayList.get(0);
        addExp = new AddExp();
        addExp.add(mulExp);
        for (int i = 0; i < length; i++) {
            temp = new AddExp();
            temp.add(addExp);
            temp.add(lexicalities.get(i));
            temp.add(arrayList.get(i + 1));
            addExp = temp;
        }
        return addExp;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (MulExp.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }
}
