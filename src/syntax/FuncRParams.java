package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class FuncRParams extends ParserUnit {
    FuncRParams() {

    }

    FuncRParams(String name, ArrayList<ParserUnit> units) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
    }

    FuncRParams(String name, ArrayList<ParserUnit> units, ArrayList<Lexicality> lexicalities) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
        this.lexicalities = new ArrayList<>(lexicalities);
    }

    public static FuncRParams parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<ParserUnit> arrayList = new ArrayList<>();
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        arrayList.add(Exp.parser(lexicalitySupporter));
        while(lexicalitySupporter.read().getType().equals("COMMA")){
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
            if(Exp.pretreat(lexicalitySupporter)){
                arrayList.add(Exp.parser(lexicalitySupporter));
            }
        }
        return new FuncRParams("FuncRParams", arrayList, lexicalities);
    }

    public void output(){
        derivations.get(0).output();
        int length=lexicalities.size();
        for(int i=0;i<length;i++){
            OutputWriter.writeln(lexicalities.get(i).toString());
            derivations.get(i+1).output();
        }
            OutputWriter.writeln(String.format("<%s>",name));

    }
    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (lexicalitySupporter.isEmpty()) {
            return false;
        }
        if (Exp.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }
}
