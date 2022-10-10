package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class ParserUnit {
    String name;
    ArrayList<ParserUnit> derivations;
    ArrayList<Lexicality> lexicalities;
    public ParserUnit() {
    }

    ParserUnit(String name, ArrayList<ParserUnit> units) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
    }

    public static ParserUnit parser(LexicalitySupporter lexicalitySupporter) {
        return new ParserUnit();
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return true;
    }

    public void output(){
        OutputWriter.writeln(String.format("<%s>",name));
    }
    public String getName(){
        return name;
    }
}
