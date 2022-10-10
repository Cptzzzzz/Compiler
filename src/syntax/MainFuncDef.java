package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class MainFuncDef extends ParserUnit {
    MainFuncDef() {

    }

    MainFuncDef(String name, ArrayList<ParserUnit> units) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
    }

    MainFuncDef(String name, ArrayList<ParserUnit> units, ArrayList<Lexicality> lexicalities) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
        this.lexicalities = new ArrayList<>(lexicalities);
    }

    public static MainFuncDef parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<ParserUnit> arrayList = new ArrayList<>();
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
        }
        if(Block.pretreat(lexicalitySupporter)){
            arrayList.add(Block.parser(lexicalitySupporter));
        }
        return new MainFuncDef("MainFuncDef", arrayList, lexicalities);
    }

    public void output(){
        for(Lexicality lexicality:lexicalities){
            OutputWriter.writeln(lexicality.toString());
        }
        if(!derivations.isEmpty()){
            derivations.get(0).output();
        }
        OutputWriter.writeln(String.format("<%s>",name));
    }
    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (lexicalitySupporter.isEmpty()) {
            return false;
        }

        int pointer = 0, length = 4,offset=0;
        String[] array = {"INTTK", "MAINTK", "LPARENT", "RPARENT"};
        if(lexicalitySupporter.read().getType().equals("INTTK")){
            offset++;
            lexicalitySupporter.next();
        }else{
            lexicalitySupporter.backspace(offset);
            return false;
        }
        if(lexicalitySupporter.read().getType().equals("MAINTK")){
            offset++;
            lexicalitySupporter.next();
        }else{
            lexicalitySupporter.backspace(offset);
            return false;
        }
        if(lexicalitySupporter.read().getType().equals("LPARENT")){
            offset++;
            lexicalitySupporter.next();
        }else{
            lexicalitySupporter.backspace(offset);
            return false;
        }
        if(lexicalitySupporter.read().getType().equals("RPARENT")){
            offset++;
            lexicalitySupporter.next();
        }else{
            lexicalitySupporter.backspace(offset);
            return false;
        }
        lexicalitySupporter.backspace(offset);
        return true;
    }
}
