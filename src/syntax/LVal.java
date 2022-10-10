package syntax;

import lexical.LexicalitySupporter;

public class LVal extends ParserUnit {
    LVal() {
        name = "LVal";
    }

    public static LVal parser(LexicalitySupporter lexicalitySupporter) {
        LVal lVal = new LVal();
        lVal.add(lexicalitySupporter.readAndNext());
        while (lexicalitySupporter.read().getType().equals("LBRACK")) {
            lVal.add(lexicalitySupporter.readAndNext());
            lVal.add(Exp.parser(lexicalitySupporter));
            lVal.add(lexicalitySupporter.readAndNext());
        }
        return lVal;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        LexicalitySupporter lexicalitySupporter1 = new LexicalitySupporter(lexicalitySupporter.getPointer());
        if (lexicalitySupporter1.read().getType().equals("IDENFR")) {
            lexicalitySupporter1.next();
        } else {
            return false;
        }
        if (lexicalitySupporter1.read().getType().equals("LPARENT")) {
            return false;
        } else {
            return true;
        }
    }
}
