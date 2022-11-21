package frontend.syntax;

import frontend.lexical.LexicalitySupporter;

public class ConstInitVal extends ParserUnit {
    ConstInitVal() {
        type = "ConstInitVal";
    }

    public static ConstInitVal parser(LexicalitySupporter lexicalitySupporter) {
        ConstInitVal constInitVal = new ConstInitVal();
        if (ConstExp.pretreat(lexicalitySupporter)) {
            constInitVal.add(ConstExp.parser(lexicalitySupporter));
        } else if (lexicalitySupporter.read().getType().equals("LBRACE")) {
            constInitVal.add(lexicalitySupporter.readAndNext());
            if (ConstInitVal.pretreat(lexicalitySupporter)) {
                constInitVal.add(ConstInitVal.parser(lexicalitySupporter));
                while (lexicalitySupporter.read().getType().equals("COMMA")) {
                    constInitVal.add(lexicalitySupporter.readAndNext());
                    constInitVal.add(ConstInitVal.parser(lexicalitySupporter));
                }
            }
            if (lexicalitySupporter.read().getType().equals("RBRACE")) {
                constInitVal.add(lexicalitySupporter.readAndNext());
            } else {
                //todo 错误处理
            }
        }
        return constInitVal;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (ConstExp.pretreat(lexicalitySupporter)) {
            return true;
        }
        return lexicalitySupporter.read().getType().equals("LBRACE");
    }
}
