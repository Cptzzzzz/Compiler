package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class ConstInitVal extends ParserUnit{
    ConstInitVal(){

    }
    ConstInitVal(String name, ArrayList<ParserUnit> units){
        this.name=name;
        this.derivations=new ArrayList<>(units);
    }
    ConstInitVal(String name, ArrayList<ParserUnit> units, ArrayList<Lexicality> lexicalities) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
        this.lexicalities = new ArrayList<>(lexicalities);
    }
    public static ConstInitVal parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<ParserUnit> arrayList = new ArrayList<>();
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        if(ConstExp.pretreat(lexicalitySupporter)){
            arrayList.add(ConstExp.parser(lexicalitySupporter));
        }else if(lexicalitySupporter.read().getType().equals("LBRACE")){
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
            if(ConstInitVal.pretreat(lexicalitySupporter)){
                arrayList.add(ConstInitVal.parser(lexicalitySupporter));
                while(lexicalitySupporter.read().getType().equals("COMMA")){
                    lexicalities.add(lexicalitySupporter.read());
                    lexicalitySupporter.next();
                    if(ConstInitVal.pretreat(lexicalitySupporter)){
                        arrayList.add(ConstInitVal.parser(lexicalitySupporter));
                    }
                }
            }
            if(lexicalitySupporter.read().getType().equals("RBRACE")){
                lexicalities.add(lexicalitySupporter.read());
                lexicalitySupporter.next();
            }
        }

        return new ConstInitVal("ConstInitVal",arrayList,lexicalities);
    }
    public void output(){
        if(lexicalities.isEmpty()){
            derivations.get(0).output();
        }else{
            OutputWriter.writeln(lexicalities.get(0).toString());
            if(derivations.isEmpty()){
                OutputWriter.writeln(lexicalities.get(1).toString());
            }else{
                derivations.get(0).output();
                int length=derivations.size();
                for(int i=1;i<length;i++){
                    OutputWriter.writeln(lexicalities.get(i).toString());
                    derivations.get(i).output();
                }
                if(length==1)
                OutputWriter.writeln(lexicalities.get(1).toString());
                else OutputWriter.writeln(lexicalities.get(length).toString());

            }
        }
        OutputWriter.writeln(String.format("<%s>",name));
    }
    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if(lexicalitySupporter.isEmpty()){
            return false;
        }
        if(ConstExp.pretreat(lexicalitySupporter)){
            return true;
        }
        if(lexicalitySupporter.read().getType().equals("LBRACE")){
            return true;
        }
        return false;
    }
}
