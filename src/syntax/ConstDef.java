package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class ConstDef extends ParserUnit{
    ConstDef(){

    }
    ConstDef(String name, ArrayList<Lexicality> lexicalities){
        this.name=name;
        this.lexicalities=new ArrayList<Lexicality>(lexicalities);
    }
    ConstDef(String name, ArrayList<ParserUnit> units, ArrayList<Lexicality> lexicalities) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
        this.lexicalities = new ArrayList<>(lexicalities);
    }
    public static ConstDef parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<ParserUnit> arrayList = new ArrayList<>();
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        lexicalities.add(lexicalitySupporter.read());
        lexicalitySupporter.next();
        while(lexicalitySupporter.read().getType().equals("LBRACK")){
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
            if(ConstExp.pretreat(lexicalitySupporter)){
                arrayList.add(ConstExp.parser(lexicalitySupporter));
            }
            if(lexicalitySupporter.read().getType().equals("RBRACK")){
                lexicalities.add(lexicalitySupporter.read());
                lexicalitySupporter.next();
            }
        }
        if(lexicalitySupporter.read().getType().equals("ASSIGN")){
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
        }
        if(ConstInitVal.pretreat(lexicalitySupporter)){
            arrayList.add(ConstInitVal.parser(lexicalitySupporter));
        }
        return new ConstDef("ConstDef",arrayList,lexicalities);
    }
    public void output(){
        OutputWriter.writeln(lexicalities.get(0).toString());
        int length=derivations.size();
        for(int i=0;i<length-1;i++){
            OutputWriter.writeln(lexicalities.get(1+2*i).toString());
            derivations.get(i).output();
            OutputWriter.writeln(lexicalities.get(2+2*i).toString());
        }
        OutputWriter.writeln(lexicalities.get(2*length-1).toString());
        derivations.get(length-1).output();
        OutputWriter.writeln(String.format("<%s>",name));
    }
    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if(lexicalitySupporter.isEmpty()){
            return false;
        }
        if(lexicalitySupporter.read().getType().equals("IDENFR")){
            return true;
        }
        return false;
    }
}
