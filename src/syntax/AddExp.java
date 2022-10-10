package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class AddExp extends ParserUnit {
    AddExp() {

    }

    AddExp(String name, ArrayList<ParserUnit> units) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
    }

    AddExp(String name, ArrayList<ParserUnit> units, ArrayList<Lexicality> lexicalities) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
        this.lexicalities = new ArrayList<>(lexicalities);
    }

    public static AddExp parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<ParserUnit> arrayList = new ArrayList<>();
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        arrayList.add(MulExp.parser(lexicalitySupporter));
        while (lexicalitySupporter.read().getType().equals("PLUS") ||
                lexicalitySupporter.read().getType().equals("MINU")) {
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
            if(MulExp.pretreat(lexicalitySupporter)){
                arrayList.add(MulExp.parser(lexicalitySupporter));
            }
        }
        return new AddExp("AddExp", arrayList, lexicalities);
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
        if (MulExp.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }
}
