package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class VarDef extends ParserUnit {
    VarDef() {

    }

    VarDef(String name, ArrayList<ParserUnit> units) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
    }

    VarDef(String name, ArrayList<ParserUnit> units, ArrayList<Lexicality> lexicalities) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
        this.lexicalities = new ArrayList<>(lexicalities);
    }

    public static VarDef parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<ParserUnit> arrayList = new ArrayList<>();
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        lexicalities.add(lexicalitySupporter.read());
        lexicalitySupporter.next();
        while(lexicalitySupporter.read().getType().equals("LBRACK")){
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
            if(ConstExp.pretreat(lexicalitySupporter)){
                arrayList.add(ConstExp.parser(lexicalitySupporter));
            }
            if(lexicalitySupporter.read().getType().equals("RBRACK")){
                lexicalities.add(lexicalitySupporter.read());
                lexicalitySupporter.next();
            }
        }
        if(lexicalitySupporter.read().getType().equals("ASSIGN")){
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
            if(InitVal.pretreat(lexicalitySupporter)){
                arrayList.add(InitVal.parser(lexicalitySupporter));
            }
        }
        return new VarDef("VarDef", arrayList, lexicalities);
    }

    public void output() {
        int lexicalityLength = lexicalities.size(), derivationLength = derivations.size();
        int lexicalityPointer = 0, derivationPointer = 0;
        OutputWriter.writeln(lexicalities.get(lexicalityPointer).toString());
        lexicalityPointer++;
        while (lexicalityPointer < lexicalityLength &&
                lexicalities.get(lexicalityPointer).getType().equals("LBRACK")) {
            OutputWriter.writeln(lexicalities.get(lexicalityPointer).toString());
            lexicalityPointer++;
            derivations.get(derivationPointer).output();
            derivationPointer++;
            OutputWriter.writeln(lexicalities.get(lexicalityPointer).toString());
            lexicalityPointer++;
        }
        if (lexicalityLength != lexicalityPointer && derivationPointer != derivationLength) {
            OutputWriter.writeln(lexicalities.get(lexicalityPointer).toString());
            derivations.get(derivationPointer).output();
        }
        OutputWriter.writeln(String.format("<%s>", name));
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
