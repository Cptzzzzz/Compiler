package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class PrimaryExp extends ParserUnit {
    PrimaryExp() {

    }

    PrimaryExp(String name, ArrayList<ParserUnit> units) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
    }

    PrimaryExp(String name, ArrayList<ParserUnit> units, ArrayList<Lexicality> lexicalities) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
        this.lexicalities = new ArrayList<>(lexicalities);
    }

    public static PrimaryExp parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<ParserUnit> arrayList = new ArrayList<>();
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        if (lexicalitySupporter.read().getType().equals("LPARENT")) {
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
            if (Exp.pretreat(lexicalitySupporter)) {
                arrayList.add(Exp.parser(lexicalitySupporter));
            }
            if (lexicalitySupporter.read().getType().equals("RPARENT")) {
                lexicalities.add(lexicalitySupporter.read());
                lexicalitySupporter.next();
            }
        } else if (LVal.pretreat(lexicalitySupporter)) {
            arrayList.add(LVal.parser(lexicalitySupporter));
        } else if (Number.pretreat(lexicalitySupporter)) {
            arrayList.add(Number.parser(lexicalitySupporter));
        }
        return new PrimaryExp("PrimaryExp", arrayList, lexicalities);
    }
    public void output(){
        if(derivations.get(0).getName().equals("Exp")){
            OutputWriter.writeln(lexicalities.get(0).toString());
            derivations.get(0).output();
            OutputWriter.writeln(lexicalities.get(1).toString());
        }else{
            derivations.get(0).output();
        }
        OutputWriter.writeln(String.format("<%s>",name));
    }
    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (lexicalitySupporter.isEmpty()) {
            return false;
        }
        if (lexicalitySupporter.read().getType().equals("LPARENT")) {
            return true;
        } else if (LVal.pretreat(lexicalitySupporter)) {
            return true;
        } else if (Number.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }
}
