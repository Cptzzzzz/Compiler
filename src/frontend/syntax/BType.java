package frontend.syntax;

import frontend.util.LexicalitySupporter;
import frontend.util.ParserUnit;

public class BType extends ParserUnit {
    BType() {
        setType("BType");
        setOutput(false);
    }

    public static BType parser(LexicalitySupporter lexicalitySupporter) {
        BType bType = new BType();
        bType.add(lexicalitySupporter.readAndNext());
        return bType;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return lexicalitySupporter.read().getType().equals("INTTK");
    }
}
