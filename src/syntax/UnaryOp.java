package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class UnaryOp extends ParserUnit{
    UnaryOp(){

    }
    UnaryOp(String name, ArrayList<Lexicality> lexicalities){
        this.name=name;
        this.lexicalities=new ArrayList<>(lexicalities);
    }

    public static UnaryOp parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        lexicalities.add(lexicalitySupporter.read());
        lexicalitySupporter.next();
        return new UnaryOp("UnaryOp",lexicalities);
    }

    public void output(){
        OutputWriter.writeln(lexicalities.get(0).toString());
        OutputWriter.writeln(String.format("<%s>",name));
    }
    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if(lexicalitySupporter.isEmpty()){
            return false;
        }
        if(lexicalitySupporter.read().getType().equals("PLUS")||
                lexicalitySupporter.read().getType().equals("MINU")||
                lexicalitySupporter.read().getType().equals("NOT")){
            return true;
        }
        return false;
    }
}
