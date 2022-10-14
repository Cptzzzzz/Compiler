package syntax;

import lexical.LexicalitySupporter;

public class InitVal extends ParserUnit {
    InitVal() {
        type = "InitVal";
    }

    public static InitVal parser(LexicalitySupporter lexicalitySupporter) {
        InitVal initVal = new InitVal();
        if (Exp.pretreat(lexicalitySupporter)) {
            initVal.add(Exp.parser(lexicalitySupporter));
        } else if (lexicalitySupporter.read().getType().equals("LBRACE")) {
            initVal.add(lexicalitySupporter.readAndNext());
            if (InitVal.pretreat(lexicalitySupporter)) {
                initVal.add(InitVal.parser(lexicalitySupporter));
                while (lexicalitySupporter.read().getType().equals("COMMA")) {
                    initVal.add(lexicalitySupporter.readAndNext());
                    initVal.add(InitVal.parser(lexicalitySupporter));
                }
            }
            initVal.add(lexicalitySupporter.readAndNext());
        }
        return initVal;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (Exp.pretreat(lexicalitySupporter)) {
            return true;
        }
        if (lexicalitySupporter.read().getType().equals("LBRACE")) {
            return true;
        }
        return false;
    }
}
