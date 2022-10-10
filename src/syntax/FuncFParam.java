package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class FuncFParam extends ParserUnit {
    FuncFParam() {

    }

    FuncFParam(String name, ArrayList<ParserUnit> units) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
    }

    FuncFParam(String name, ArrayList<ParserUnit> units, ArrayList<Lexicality> lexicalities) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
        this.lexicalities = new ArrayList<>(lexicalities);
    }

    public static FuncFParam parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<ParserUnit> arrayList = new ArrayList<>();
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        arrayList.add(BType.parser(lexicalitySupporter));
        if (lexicalitySupporter.read().getType().equals("IDENFR")) {
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
            if (lexicalitySupporter.read().getType().equals("LBRACK")) {
                lexicalities.add(lexicalitySupporter.read());
                lexicalitySupporter.next();
                if (lexicalitySupporter.read().getType().equals("RBRACK")) {
                    lexicalities.add(lexicalitySupporter.read());
                    lexicalitySupporter.next();
                    while (lexicalitySupporter.read().getType().equals("LBRACK")) {
                        lexicalities.add(lexicalitySupporter.read());
                        lexicalitySupporter.next();
                        if (ConstExp.pretreat(lexicalitySupporter)) {
                            arrayList.add(ConstExp.parser(lexicalitySupporter));
                        }
                        if (lexicalitySupporter.read().getType().equals("RBRACK")) {
                            lexicalities.add(lexicalitySupporter.read());
                            lexicalitySupporter.next();
                        }
                    }
                }
            }
        }
        return new FuncFParam("FuncFParam", arrayList, lexicalities);
    }

    public void output() {
        derivations.get(0).output();
        OutputWriter.writeln(lexicalities.get(0).toString());
        int length=(lexicalities.size()-1)/2;
        if(length>0){
            OutputWriter.writeln(lexicalities.get(1).toString());
            OutputWriter.writeln(lexicalities.get(2).toString());
            if(length>1){
                for(int i=1;i<length;i++){
                    OutputWriter.writeln(lexicalities.get(2*i+1).toString());
                    derivations.get(i).output();
                    OutputWriter.writeln(lexicalities.get(2*i+2).toString());
                }
            }
        }
        OutputWriter.writeln(String.format("<%s>",name));
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (lexicalitySupporter.isEmpty()) {
            return false;
        }
        if (BType.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }
}
