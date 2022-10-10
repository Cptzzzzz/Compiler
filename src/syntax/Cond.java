package syntax;

import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class Cond extends ParserUnit{
    Cond(){

    }
    Cond(String name, ArrayList<ParserUnit> units){
        this.name=name;
        this.derivations=new ArrayList<>(units);
    }

    public static Cond parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<ParserUnit> arrayList = new ArrayList<>();
        arrayList.add(LOrExp.parser(lexicalitySupporter));
        return new Cond("Cond",arrayList);
    }

    public void output(){
        derivations.get(0).output();
        OutputWriter.writeln(String.format("<%s>",name));
    }
    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if(lexicalitySupporter.isEmpty()){
            return false;
        }
        if(LOrExp.pretreat(lexicalitySupporter)){
            return true;
        }
        return false;
    }
}
