package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class VarDecl extends ParserUnit {
    VarDecl() {

    }

    VarDecl(String name, ArrayList<ParserUnit> units) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
    }

    VarDecl(String name, ArrayList<ParserUnit> units, ArrayList<Lexicality> lexicalities) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
        this.lexicalities = new ArrayList<>(lexicalities);
    }

    public static VarDecl parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<ParserUnit> arrayList = new ArrayList<>();
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        if (BType.pretreat(lexicalitySupporter)) {
            arrayList.add(BType.parser(lexicalitySupporter));
        } else {

        }
        if (VarDef.pretreat(lexicalitySupporter)) {
            arrayList.add(VarDef.parser(lexicalitySupporter));
        } else {

        }
        while (lexicalitySupporter.read().getType().equals("COMMA")) {
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
            if (VarDef.pretreat(lexicalitySupporter)) {
                arrayList.add(VarDef.parser(lexicalitySupporter));
            } else {

            }
        }
        if (lexicalitySupporter.read().getType().equals("SEMICN")) {
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
        }
        return new VarDecl("VarDecl", arrayList, lexicalities);
    }

    public void output() {
        int lexicalityLength = lexicalities.size(), derivationLength = derivations.size();
        int lexicalityPointer = 0, derivationPointer = 0;
        derivations.get(derivationPointer).output();
        derivationPointer++;
        derivations.get(derivationPointer).output();
        derivationPointer++;
        for (; lexicalityPointer < lexicalityLength && derivationPointer < derivationLength;
             lexicalityPointer++, derivationPointer++) {
            OutputWriter.writeln(lexicalities.get(lexicalityPointer).toString());
            derivations.get(derivationPointer).output();
        }
        if(lexicalityPointer<lexicalityLength){
            OutputWriter.writeln(lexicalities.get(lexicalityPointer).toString());
        }
        OutputWriter.writeln(String.format("<%s>", name));
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (lexicalitySupporter.isEmpty()) {
            return false;
        }
        int offset = 0;
        if (BType.pretreat(lexicalitySupporter)) {
            offset += 1;
            lexicalitySupporter.next();
        } else {
            return false;
        }
        if (VarDef.pretreat(lexicalitySupporter)) {
            lexicalitySupporter.backspace(offset);
            return true;
        } else {
            lexicalitySupporter.backspace(offset);
            return false;
        }
    }
}
