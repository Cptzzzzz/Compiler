package syntax;

import lexical.LexicalitySupporter;

public class BType extends ParserUnit {
    BType() {
        type = "BType";
    }

    public static BType parser(LexicalitySupporter lexicalitySupporter) {
        BType bType = new BType();
        bType.add(lexicalitySupporter.readAndNext());
        return bType;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (lexicalitySupporter.read().getType().equals("INTTK")) {
            return true;
        }
        return false;
    }
}
