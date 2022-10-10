package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class Block extends ParserUnit {
    Block() {

    }

    Block(String name, ArrayList<ParserUnit> units) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
    }

    Block(String name, ArrayList<ParserUnit> units, ArrayList<Lexicality> lexicalities) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
        this.lexicalities = new ArrayList<>(lexicalities);
    }

    public static Block parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<ParserUnit> arrayList = new ArrayList<>();
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        lexicalities.add(lexicalitySupporter.read());
        lexicalitySupporter.next();
        while(BlockItem.pretreat(lexicalitySupporter)){
            arrayList.add(BlockItem.parser(lexicalitySupporter));
        }
        if(lexicalitySupporter.read().getType().equals("RBRACE")){
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
        }else{

        }
        return new Block("Block",arrayList,lexicalities);
    }
    public void output(){
        OutputWriter.writeln(lexicalities.get(0).toString());
        for(ParserUnit parserUnit:derivations){
            parserUnit.output();
        }
        OutputWriter.writeln(lexicalities.get(1).toString());
        OutputWriter.writeln(String.format("<%s>",name));
    }
    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (lexicalitySupporter.isEmpty()) {
            return false;
        }
        if (lexicalitySupporter.read().getType().equals("LBRACE")) {
            return true;
        }
        return false;
    }
}
