package frontend.syntax;

import frontend.lexical.LexicalitySupporter;

public class ConstDef extends ParserUnit {
    ConstDef() {
        type = "ConstDef";
    }

    public static ConstDef parser(LexicalitySupporter lexicalitySupporter) {
        ConstDef constDef = new ConstDef();
        constDef.add(lexicalitySupporter.readAndNext());
        while (lexicalitySupporter.read().getType().equals("LBRACK")) {
            constDef.add(lexicalitySupporter.readAndNext());
            if (ConstExp.pretreat(lexicalitySupporter)) {
                constDef.add(ConstExp.parser(lexicalitySupporter));
            }
            constDef.add(lexicalitySupporter.readAndNext());
        }
        if (lexicalitySupporter.read().getType().equals("ASSIGN")) {
            constDef.add(lexicalitySupporter.readAndNext());
            constDef.add(ConstInitVal.parser(lexicalitySupporter));
        }
        return constDef;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return lexicalitySupporter.read().getType().equals("IDENFR");
    }
}
