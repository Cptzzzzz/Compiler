package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class InitVal extends ParserUnit{
    InitVal(){

    }
    InitVal(String name, ArrayList<ParserUnit> units){
        this.name=name;
        this.derivations=new ArrayList<>(units);
    }
    InitVal(String name, ArrayList<ParserUnit> units, ArrayList<Lexicality> lexicalities) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
        this.lexicalities = new ArrayList<>(lexicalities);
    }
    public static InitVal parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<ParserUnit> arrayList = new ArrayList<>();
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        if(Exp.pretreat(lexicalitySupporter)){
            arrayList.add(Exp.parser(lexicalitySupporter));
        } else if(lexicalitySupporter.read().getType().equals("LBRACE")){
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
            if(InitVal.pretreat(lexicalitySupporter)){
                arrayList.add(InitVal.parser(lexicalitySupporter));
                while(lexicalitySupporter.read().getType().equals("COMMA")){
                    lexicalities.add(lexicalitySupporter.read());
                    lexicalitySupporter.next();
                    if(InitVal.pretreat(lexicalitySupporter)){
                        arrayList.add(InitVal.parser(lexicalitySupporter));
                    }
                }
            }
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
        }
        return new InitVal("InitVal",arrayList,lexicalities);
    }
    public void output(){
        if(lexicalities.isEmpty()){
            derivations.get(0).output();
        }else{
            OutputWriter.writeln(lexicalities.get(0).toString());
            int length=derivations.size();
            if(length>0){
                derivations.get(0).output();
            }
            for(int i=1;i<length;i++){
                OutputWriter.writeln(lexicalities.get(i).toString());
                derivations.get(i).output();
            }
            OutputWriter.writeln(lexicalities.get(length).toString());
        }
        OutputWriter.writeln(String.format("<%s>",name));
    }
    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if(Exp.pretreat(lexicalitySupporter)){
            return true;
        }
        if(lexicalitySupporter.read().getType().equals("LBRACE")){
            return true;
        }
        return false;
    }
}
