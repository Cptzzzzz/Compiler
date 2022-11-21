package frontend.syntax;

import frontend.lexical.LexicalitySupporter;

public class ConstDecl extends ParserUnit {
    ConstDecl() {
        type = "ConstDecl";
    }

    public static ConstDecl parser(LexicalitySupporter lexicalitySupporter) {
        ConstDecl constDecl = new ConstDecl();
        constDecl.add(lexicalitySupporter.readAndNext());
        constDecl.add(BType.parser(lexicalitySupporter));
        constDecl.add(ConstDef.parser(lexicalitySupporter));
        while (lexicalitySupporter.read().getType().equals("COMMA")) {
            constDecl.add(lexicalitySupporter.readAndNext());
            constDecl.add(ConstDef.parser(lexicalitySupporter));
        }
        if (lexicalitySupporter.read().getType().equals("SEMICN")) {
            constDecl.add(lexicalitySupporter.readAndNext());
        } else {
            //todo 缺少分号
        }
        return constDecl;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return lexicalitySupporter.read().getType().equals("CONSTTK");
    }
}
