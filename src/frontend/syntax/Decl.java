package frontend.syntax;

import frontend.util.LexicalitySupporter;
import frontend.util.ParserUnit;

public class Decl extends ParserUnit {
    Decl() {
        setType("Decl");
        setOutput(false);
    }

    public static Decl parser(LexicalitySupporter lexicalitySupporter) {
        Decl decl = new Decl();
        if (ConstDecl.pretreat(lexicalitySupporter)) {
            decl.add(ConstDecl.parser(lexicalitySupporter));
        } else if (VarDecl.pretreat(lexicalitySupporter)) {
            decl.add(VarDecl.parser(lexicalitySupporter));
        }
        return decl;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return ConstDecl.pretreat(lexicalitySupporter) || VarDecl.pretreat(lexicalitySupporter);
    }
}
