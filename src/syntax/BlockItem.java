package syntax;

import lexical.LexicalitySupporter;

import java.util.ArrayList;

public class BlockItem extends ParserUnit{
    BlockItem(){

    }
    BlockItem(String name, ArrayList<ParserUnit> units){
        this.name=name;
        this.derivations=new ArrayList<>(units);
    }

    public static BlockItem parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<ParserUnit> arrayList = new ArrayList<>();

        if(Decl.pretreat(lexicalitySupporter)){
            arrayList.add(Decl.parser(lexicalitySupporter));
        }else if(Stmt.pretreat(lexicalitySupporter)){
            arrayList.add(Stmt.parser(lexicalitySupporter));
        }

        return new BlockItem("BlockItem",arrayList);
    }
    public void output(){
        for(ParserUnit parserUnit:derivations){
            parserUnit.output();
        }
    }
    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if(lexicalitySupporter.isEmpty()){
            return false;
        }
        if(Decl.pretreat(lexicalitySupporter)||Stmt.pretreat(lexicalitySupporter)){
            return true;
        }
        return false;
    }
}
