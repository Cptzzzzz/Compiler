package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.Error;
import util.ErrorWriter;

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
            constDecl.add(new Lexicality(";","SEMICN"));
            ErrorWriter.add(new Error(lexicalitySupporter.getLastLineNumber(), 'i'));
        }
        return constDecl;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (lexicalitySupporter.read().getType().equals("CONSTTK")) {
            return true;
        }
        return false;
    }
}
