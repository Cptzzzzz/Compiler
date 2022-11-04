package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;

public class Number extends ParserUnit {
    Number() {
        type = "Number";
    }

    public static Number parser(LexicalitySupporter lexicalitySupporter) {
        Number number = new Number();
        number.add(lexicalitySupporter.readAndNext());
        return number;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (lexicalitySupporter.read().getType().equals("INTCON")) {
            return true;
        }
        return false;
    }

    public int getValue(){
        return Integer.valueOf(((Lexicality)nodes.get(0)).getContent());
    }

    public String generateIntermediateCode(){
        return ((Lexicality)nodes.get(0)).getContent();
    }
}
