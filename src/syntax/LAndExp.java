package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class LAndExp extends ParserUnit{
    LAndExp(){

    }
    LAndExp(String name, ArrayList<ParserUnit> units){
        this.name=name;
        this.derivations=new ArrayList<>(units);
    }
    LAndExp(String name, ArrayList<ParserUnit> units, ArrayList<Lexicality> lexicalities) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
        this.lexicalities = new ArrayList<>(lexicalities);
    }
    public static LAndExp parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<ParserUnit> arrayList = new ArrayList<>();
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        arrayList.add(EqExp.parser(lexicalitySupporter));
        while(lexicalitySupporter.read().getType().equals("AND")){
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
            if(EqExp.pretreat(lexicalitySupporter)){
                arrayList.add(EqExp.parser(lexicalitySupporter));
            }
        }
        return new LAndExp("LAndExp",arrayList,lexicalities);
    }
    public void output(){
        derivations.get(0).output();
        int length=lexicalities.size();
        for(int i=0;i<length;i++){
            OutputWriter.writeln(String.format("<%s>",name));
            OutputWriter.writeln(lexicalities.get(i).toString());
            derivations.get(i+1).output();
        }
        OutputWriter.writeln(String.format("<%s>",name));
    }
    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if(lexicalitySupporter.isEmpty()){
            return false;
        }
        if(EqExp.pretreat(lexicalitySupporter)){
            return true;
        }
        return false;
    }
}
