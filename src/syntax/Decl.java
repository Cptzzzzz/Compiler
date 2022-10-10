package syntax;

import lexical.LexicalitySupporter;
import util.CompilerMode;

public class Decl extends ParserUnit {
    Decl() {
        name = "Decl";
        isOutput=false;
    }

    public static Decl parser(LexicalitySupporter lexicalitySupporter) {
        if(CompilerMode.getDebug())
            System.out.println("Decl");
        Decl decl = new Decl();
        if (ConstDecl.pretreat(lexicalitySupporter)) {
            decl.add(ConstDecl.parser(lexicalitySupporter));
        } else if (VarDecl.pretreat(lexicalitySupporter)) {
            decl.add(VarDecl.parser(lexicalitySupporter));
        }
        return decl;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (ConstDecl.pretreat(lexicalitySupporter) || VarDecl.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }
}
