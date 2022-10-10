package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class FuncType extends ParserUnit{
    FuncType(){

    }
    FuncType(String name, ArrayList<Lexicality> lexicalities){
        this.name=name;
        this.lexicalities=new ArrayList<>(lexicalities);
    }

    public static FuncType parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        lexicalities.add(lexicalitySupporter.read());
        lexicalitySupporter.next();
        return new FuncType("FuncType",lexicalities);
    }

    public void output(){
        OutputWriter.writeln(lexicalities.get(0).toString());
        OutputWriter.writeln(String.format("<%s>",name));
    }
    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if(lexicalitySupporter.isEmpty()){
            return false;
        }
        if(lexicalitySupporter.read().getType().equals("VOIDTK")||
                lexicalitySupporter.read().getType().equals("INTTK")){
            return true;
        }
        return false;
    }
}
