package syntax;

import lexical.LexicalitySupporter;

public class FuncType extends ParserUnit {
    FuncType() {
        type = "FuncType";
    }

    public static FuncType parser(LexicalitySupporter lexicalitySupporter) {
        FuncType funcType = new FuncType();
        funcType.add(lexicalitySupporter.readAndNext());
        return funcType;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (lexicalitySupporter.read().getType().equals("VOIDTK") ||
                lexicalitySupporter.read().getType().equals("INTTK")) {
            return true;
        }
        return false;
    }
}
