package frontend.syntax;

import frontend.util.LexicalitySupporter;
import frontend.util.ParserUnit;

public class UnaryOp extends ParserUnit {
    UnaryOp() {
        setType("UnaryOp");
    }

    public static UnaryOp parser(LexicalitySupporter lexicalitySupporter) {
        UnaryOp unaryOp = new UnaryOp();
        unaryOp.add(lexicalitySupporter.readAndNext());
        return unaryOp;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return lexicalitySupporter.read().getType().equals("PLUS") ||
                lexicalitySupporter.read().getType().equals("MINU") ||
                lexicalitySupporter.read().getType().equals("NOT");
    }
}
