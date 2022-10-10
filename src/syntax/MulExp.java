package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class MulExp extends ParserUnit {
    MulExp() {

    }

    MulExp(String name, ArrayList<ParserUnit> units) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
    }

    MulExp(String name, ArrayList<ParserUnit> units, ArrayList<Lexicality> lexicalities) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
        this.lexicalities = new ArrayList<>(lexicalities);
    }

    public static MulExp parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<ParserUnit> arrayList = new ArrayList<>();
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        arrayList.add(UnaryExp.parser(lexicalitySupporter));
        while (lexicalitySupporter.read().getType().equals("MULT") ||
                lexicalitySupporter.read().getType().equals("DIV") ||
                lexicalitySupporter.read().getType().equals("MOD")) {
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
            if (UnaryExp.pretreat(lexicalitySupporter)) {
                arrayList.add(UnaryExp.parser(lexicalitySupporter));
            }
        }
        return new MulExp("MulExp", arrayList, lexicalities);
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
        if (UnaryExp.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }
}
