package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class LOrExp extends ParserUnit {
    LOrExp() {

    }

    LOrExp(String name, ArrayList<ParserUnit> units) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
    }

    LOrExp(String name, ArrayList<ParserUnit> units, ArrayList<Lexicality> lexicalities) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
        this.lexicalities = new ArrayList<>(lexicalities);
    }

    public static LOrExp parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<ParserUnit> arrayList = new ArrayList<>();
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        arrayList.add(LAndExp.parser(lexicalitySupporter));
        while(lexicalitySupporter.read().getType().equals("OR")){
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
            if(LAndExp.pretreat(lexicalitySupporter)){
                arrayList.add(LAndExp.parser(lexicalitySupporter));
            }
        }
        return new LOrExp("LOrExp",arrayList,lexicalities);
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
        if (lexicalitySupporter.isEmpty()) {
            return false;
        }
        if (LAndExp.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }
}
