package syntax;

import lexical.LexicalitySupporter;

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
            constInitVal.add(lexicalitySupporter.readAndNext());
        }
        return constInitVal;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (ConstExp.pretreat(lexicalitySupporter)) {
            return true;
        }
        if (lexicalitySupporter.read().getType().equals("LBRACE")) {
            return true;
        }
        return false;
    }
}
