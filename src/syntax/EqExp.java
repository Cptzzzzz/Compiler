package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class EqExp extends ParserUnit {
    EqExp() {

    }

    EqExp(String name, ArrayList<ParserUnit> units) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
    }

    EqExp(String name, ArrayList<ParserUnit> units, ArrayList<Lexicality> lexicalities) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
        this.lexicalities = new ArrayList<>(lexicalities);
    }

    public static EqExp parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<ParserUnit> arrayList = new ArrayList<>();
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        arrayList.add(RelExp.parser(lexicalitySupporter));
        while (lexicalitySupporter.read().getType().equals("EQL") ||
                lexicalitySupporter.read().getType().equals("NEQ")) {
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
            if (RelExp.pretreat(lexicalitySupporter)) {
                arrayList.add(RelExp.parser(lexicalitySupporter));
            }
        }
        return new EqExp("EqExp", arrayList, lexicalities);
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
        if (RelExp.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }
}
