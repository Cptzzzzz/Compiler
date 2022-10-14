package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.Error;
import util.ErrorWriter;

public class VarDecl extends ParserUnit {
    VarDecl() {
        type = "VarDecl";
    }

    public static VarDecl parser(LexicalitySupporter lexicalitySupporter) {
        VarDecl varDecl = new VarDecl();
        varDecl.add(BType.parser(lexicalitySupporter));
        varDecl.add(VarDef.parser(lexicalitySupporter));
        while (lexicalitySupporter.read().getType().equals("COMMA")) {
            varDecl.add(lexicalitySupporter.readAndNext());
            varDecl.add(VarDef.parser(lexicalitySupporter));
        }
        if (lexicalitySupporter.read().getType().equals("SEMICN")) {
            varDecl.add(lexicalitySupporter.readAndNext());
        } else {
            varDecl.add(new Lexicality(";", "SEMICN"));
            ErrorWriter.add(new Error(lexicalitySupporter.getLastLineNumber(), 'i'));
        }
        return varDecl;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        LexicalitySupporter lexicalitySupporter1 = new LexicalitySupporter(lexicalitySupporter.getPointer());
        if (BType.pretreat(lexicalitySupporter1)) {
            BType.parser(lexicalitySupporter1);
        } else {
            return false;
        }
        if (VarDef.pretreat(lexicalitySupporter1)) {
            return true;
        } else {
            return false;
        }
    }
}
