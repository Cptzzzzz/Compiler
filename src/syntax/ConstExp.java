package syntax;

import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class ConstExp extends ParserUnit{
    ConstExp(){

    }
    ConstExp(String name, ArrayList<ParserUnit> units){
        this.name=name;
        this.derivations=new ArrayList<>(units);
    }

    public static ConstExp parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<ParserUnit> arrayList=new ArrayList<>();
        arrayList.add(AddExp.parser(lexicalitySupporter));
        return new ConstExp("ConstExp",arrayList);
    }

    public void output(){
        derivations.get(0).output();
        OutputWriter.writeln(String.format("<%s>",name));
    }
    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if(AddExp.pretreat(lexicalitySupporter)){
            return true;
        }
        return false;
    }
}
