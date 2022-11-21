package frontend.syntax;

import frontend.lexical.LexicalitySupporter;

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
        return Integer.parseInt(nodes.get(0).getContent());
    }
}
