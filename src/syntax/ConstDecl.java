package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class ConstDecl extends ParserUnit {
    ConstDecl() {

    }

    ConstDecl(String name, ArrayList<ParserUnit> units) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
    }

    ConstDecl(String name, ArrayList<ParserUnit> units, ArrayList<Lexicality> lexicalities) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
        this.lexicalities = new ArrayList<>(lexicalities);
    }

    public static ConstDecl parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<ParserUnit> arrayList = new ArrayList<>();
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        lexicalities.add(lexicalitySupporter.read());
        lexicalitySupporter.next();
        if (BType.pretreat(lexicalitySupporter)) {
            arrayList.add(BType.parser(lexicalitySupporter));
        } else {

        }
        if (ConstDef.pretreat(lexicalitySupporter)) {
            arrayList.add(ConstDef.parser(lexicalitySupporter));
        } else {

        }
        while (lexicalitySupporter.read().getType().equals("COMMA")) {
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
            if (ConstDef.pretreat(lexicalitySupporter)) {
                arrayList.add(ConstDef.parser(lexicalitySupporter));
            }
        }
        if (lexicalitySupporter.read().getType().equals("SEMICN")) {
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
        }
        return new ConstDecl("ConstDecl", arrayList, lexicalities);
    }

    public void output(){
        OutputWriter.writeln(lexicalities.get(0).toString());
        derivations.get(0).output();
        derivations.get(1).output();
        int length=lexicalities.size();
        for(int i=1;i<length-1;i++){
            OutputWriter.writeln(lexicalities.get(i).toString());
            derivations.get(i+1).output();
        }
        OutputWriter.writeln(lexicalities.get(length-1).toString());
        OutputWriter.writeln(String.format("<%s>",name));
    }
    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (lexicalitySupporter.isEmpty()) {
            return false;
        }
        if (lexicalitySupporter.read().getType().equals("CONSTTK")) {
            return true;
        }
        return false;
    }
}
