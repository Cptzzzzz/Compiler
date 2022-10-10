package syntax;

import lexical.LexicalitySupporter;

public class UnaryOp extends ParserUnit {
    UnaryOp() {
        name = "UnaryOp";
    }

    public static UnaryOp parser(LexicalitySupporter lexicalitySupporter) {
        UnaryOp unaryOp = new UnaryOp();
        unaryOp.add(lexicalitySupporter.readAndNext());
        return unaryOp;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (lexicalitySupporter.read().getType().equals("PLUS") ||
                lexicalitySupporter.read().getType().equals("MINU") ||
                lexicalitySupporter.read().getType().equals("NOT")) {
            return true;
        }
        return false;
    }
}
