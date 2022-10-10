package syntax;

import lexical.LexicalitySupporter;

import java.util.ArrayList;

public class Decl extends ParserUnit {
    Decl() {

    }

    Decl(String name, ArrayList<ParserUnit> units) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
    }

    public static Decl parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<ParserUnit> arrayList = new ArrayList<>();
        if (ConstDecl.pretreat(lexicalitySupporter)) {
            arrayList.add(ConstDecl.parser(lexicalitySupporter));
        } else if (VarDecl.pretreat(lexicalitySupporter)) {
            arrayList.add(VarDecl.parser(lexicalitySupporter));
        }
        return new Decl("Decl",arrayList);
    }
    public void output(){
        for(ParserUnit parserUnit:derivations){
            parserUnit.output();
        }
    }
    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (ConstDecl.pretreat(lexicalitySupporter) || VarDecl.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }
}
