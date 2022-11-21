package frontend.syntax;

import frontend.lexical.LexicalitySupporter;
import util.CompilerMode;

public class Decl extends ParserUnit {
    Decl() {
        setType("Decl");
        setOutput(false);
    }

    public static Decl parser(LexicalitySupporter lexicalitySupporter) {
        if(CompilerMode.getInstance().isDebug())
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
        return ConstDecl.pretreat(lexicalitySupporter) || VarDecl.pretreat(lexicalitySupporter);
    }
}
