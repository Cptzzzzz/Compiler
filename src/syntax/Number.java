package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class Number extends ParserUnit{
    Number(){

    }
    Number(String name, ArrayList<Lexicality> lexicalities){
        this.name=name;
        this.lexicalities=new ArrayList<>(lexicalities);
    }

    public static Number parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        lexicalities.add(lexicalitySupporter.read());
        lexicalitySupporter.next();
        return new Number("Number",lexicalities);
    }

    public void output(){
        OutputWriter.writeln(lexicalities.get(0).toString());
        OutputWriter.writeln(String.format("<%s>",name));
    }
    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if(lexicalitySupporter.isEmpty()){
            return false;
        }
        if(lexicalitySupporter.read().getType().equals("INTCON")){
            return true;
        }
        return false;
    }
}
