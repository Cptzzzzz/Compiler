package frontend.syntax;

import frontend.lexical.Lexicality;
import frontend.util.LexicalitySupporter;
import frontend.util.ParserUnit;
import util.ErrorWriter;

public class ConstDecl extends ParserUnit {
    ConstDecl() {
        setType("ConstDecl");
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
            constDecl.add(new Lexicality(";", "SEMICN"));
            ErrorWriter.add(lexicalitySupporter.getLastLineNumber(), 'i');
        }
        return constDecl;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return lexicalitySupporter.read().getType().equals("CONSTTK");
    }
}
