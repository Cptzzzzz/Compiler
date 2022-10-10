package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class LVal extends ParserUnit {
    LVal() {

    }

    LVal(String name, ArrayList<ParserUnit> units) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
    }

    LVal(String name, ArrayList<ParserUnit> units, ArrayList<Lexicality> lexicalities) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
        this.lexicalities = new ArrayList<>(lexicalities);
    }

    public static LVal parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<ParserUnit> arrayList = new ArrayList<>();
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        lexicalities.add(lexicalitySupporter.read());
        lexicalitySupporter.next();
        while (lexicalitySupporter.read().getType().equals("LBRACK")) {
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
            if (Exp.pretreat(lexicalitySupporter)) {
                arrayList.add(Exp.parser(lexicalitySupporter));
            }
            if (lexicalitySupporter.read().getType().equals("RBRACK")) {
                lexicalities.add(lexicalitySupporter.read());
                lexicalitySupporter.next();
            }
        }
        return new LVal("LVal", arrayList, lexicalities);
    }
    public void output(){
        OutputWriter.writeln(lexicalities.get(0).toString());
        int length=derivations.size();
        for(int i=0;i<length;i++){
            OutputWriter.writeln(lexicalities.get(2*i+1).toString());
            derivations.get(i).output();
            OutputWriter.writeln(lexicalities.get(2*i+2).toString());
        }
        OutputWriter.writeln(String.format("<%s>",name));
    }
    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (lexicalitySupporter.isEmpty()) {
            return false;
        }
        int offset=0;
        if (lexicalitySupporter.read().getType().equals("IDENFR")) {
            lexicalitySupporter.next();
            offset++;
        }else{
            return false;
        }
        if(lexicalitySupporter.read().getType().equals("LPARENT")){
            lexicalitySupporter.backspace(offset);
            return false;
        }else{
            lexicalitySupporter.backspace(offset);
            return true;
        }
    }
}
