package syntax;

import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class CompUnit extends ParserUnit{

    CompUnit(){

    }
    CompUnit(String name, ArrayList<ParserUnit> units){
        this.name=name;
        this.derivations=new ArrayList<>(units);
    }

    public static CompUnit parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<ParserUnit> arrayList=new ArrayList<ParserUnit>();
        while(Decl.pretreat(lexicalitySupporter)){
            arrayList.add(Decl.parser(lexicalitySupporter));
        }
        while(FuncDef.pretreat(lexicalitySupporter)){
            arrayList.add(FuncDef.parser(lexicalitySupporter));
        }
        if(MainFuncDef.pretreat(lexicalitySupporter)){
            arrayList.add(MainFuncDef.parser(lexicalitySupporter));
        }else{

        }
        return new CompUnit("CompUnit",arrayList);
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {

        return true;
    }


    public void output(){
        for(ParserUnit parserUnit:derivations){
            parserUnit.output();
        }
        OutputWriter.writeln(String.format("<%s>",name));
    }
}
