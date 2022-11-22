package frontend.syntax;

import frontend.util.LexicalitySupporter;
import frontend.util.ParserUnit;

public class FuncType extends ParserUnit {
    FuncType() {
        setType("FuncType");
    }

    public static FuncType parser(LexicalitySupporter lexicalitySupporter) {
        FuncType funcType = new FuncType();
        funcType.add(lexicalitySupporter.readAndNext());
        return funcType;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return lexicalitySupporter.read().getType().equals("VOIDTK") ||
                lexicalitySupporter.read().getType().equals("INTTK");
    }
}
