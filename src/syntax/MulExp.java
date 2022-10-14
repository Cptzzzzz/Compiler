package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;

import java.util.ArrayList;

public class MulExp extends ParserUnit {
    MulExp() {
        type ="MulExp";
    }

    public static MulExp parser(LexicalitySupporter lexicalitySupporter) {
        MulExp mulExp, temp;
        UnaryExp unaryExp;
        ArrayList<UnaryExp> arrayList = new ArrayList<>();
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        arrayList.add(UnaryExp.parser(lexicalitySupporter));
        while (lexicalitySupporter.read().getType().equals("MULT") ||
                lexicalitySupporter.read().getType().equals("DIV") ||
                lexicalitySupporter.read().getType().equals("MOD")) {
            lexicalities.add(lexicalitySupporter.readAndNext());
                arrayList.add(UnaryExp.parser(lexicalitySupporter));
        }
        int length=lexicalities.size();
        unaryExp = arrayList.get(0);
        mulExp=new MulExp();
        mulExp.add(unaryExp);
        for(int i=0;i<length;i++){
            temp=new MulExp();
            temp.add(mulExp);
            temp.add(lexicalities.get(i));
            temp.add(arrayList.get(i+1));
            mulExp=temp;
        }
        return mulExp;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (UnaryExp.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }
}
