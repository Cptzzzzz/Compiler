package frontend.syntax;

import frontend.lexical.LexicalitySupporter;

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
            if (lexicalitySupporter.read().getType().equals("RBRACE")) {
                initVal.add(lexicalitySupporter.readAndNext());
            } else {
                //todo 错误处理
            }
        }
        return initVal;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (Exp.pretreat(lexicalitySupporter)) {
            return true;
        }
        return lexicalitySupporter.read().getType().equals("LBRACE");
    }
}
