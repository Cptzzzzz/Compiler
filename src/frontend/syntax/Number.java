package frontend.syntax;

import frontend.util.LexicalitySupporter;
import frontend.util.ParserUnit;

public class Number extends ParserUnit {
    Number() {
        setType("Number");
    }

    public static Number parser(LexicalitySupporter lexicalitySupporter) {
        Number number = new Number();
        number.add(lexicalitySupporter.readAndNext());
        return number;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return lexicalitySupporter.read().getType().equals("INTCON");
    }

    public int getInteger() {
        return Integer.parseInt(getNode(0).getContent());
    }
}
