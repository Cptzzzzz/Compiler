package frontend.syntax;

import frontend.lexical.LexicalitySupporter;

public class CompUnit extends ParserUnit {
    CompUnit() {
        setType("CompUnit");
    }

    public static CompUnit parser(LexicalitySupporter lexicalitySupporter) {
        CompUnit compUnit = new CompUnit();
        while (Decl.pretreat(lexicalitySupporter)) {
            compUnit.add(Decl.parser(lexicalitySupporter));
        }
        while (FuncDef.pretreat(lexicalitySupporter)) {
            compUnit.add(FuncDef.parser(lexicalitySupporter));
        }
        compUnit.add(MainFuncDef.parser(lexicalitySupporter));
        return compUnit;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return true;
    }
}