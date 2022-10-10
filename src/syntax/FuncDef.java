package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class FuncDef extends ParserUnit {
    FuncDef() {

    }

    FuncDef(String name, ArrayList<ParserUnit> units) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
    }

    FuncDef(String name, ArrayList<ParserUnit> units, ArrayList<Lexicality> lexicalities) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
        this.lexicalities = new ArrayList<>(lexicalities);
    }

    public static FuncDef parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<ParserUnit> arrayList = new ArrayList<>();
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        arrayList.add(FuncType.parser(lexicalitySupporter));
        if (lexicalitySupporter.read().getType().equals("IDENFR")) {
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
        }
        if (lexicalitySupporter.read().getType().equals("LPARENT")) {
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
        }
        if (FuncFParams.pretreat(lexicalitySupporter)) {
            arrayList.add(FuncFParams.parser(lexicalitySupporter));
        }
        if (lexicalitySupporter.read().getType().equals("RPARENT")) {
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
        }
        if (Block.pretreat(lexicalitySupporter)) {
            arrayList.add(Block.parser(lexicalitySupporter));
        }
        return new FuncDef("FuncDef", arrayList, lexicalities);
    }

    public void output(){
        derivations.get(0).output();
        OutputWriter.writeln(lexicalities.get(0).toString());
        if(derivations.size()==3){
            OutputWriter.writeln(lexicalities.get(1).toString());
            derivations.get(1).output();
            OutputWriter.writeln(lexicalities.get(2).toString());
            derivations.get(2).output();
        }else{
            OutputWriter.writeln(lexicalities.get(1).toString());
            OutputWriter.writeln(lexicalities.get(2).toString());
            derivations.get(1).output();
        }
        OutputWriter.writeln(String.format("<%s>",name));
    }
    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (lexicalitySupporter.isEmpty()) {
            return false;
        }
        int offset = 0;
        if (FuncType.pretreat(lexicalitySupporter)) {
            offset++;
            lexicalitySupporter.next();
        } else {
            return false;
        }
        if (lexicalitySupporter.read().getType().equals("IDENFR")) {
            lexicalitySupporter.backspace(offset);
            return true;
        } else {
            lexicalitySupporter.backspace(offset);
            return false;
        }
    }
}
