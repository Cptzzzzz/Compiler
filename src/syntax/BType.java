package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class BType extends ParserUnit {
    BType() {

    }

    BType(String name, ArrayList<Lexicality> lexicalities) {
        this.name = name;
        this.lexicalities = new ArrayList<Lexicality>(lexicalities);
    }

    public static BType parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        lexicalities.add(lexicalitySupporter.read());
        lexicalitySupporter.next();
        return new BType("BType", lexicalities);
    }
    public void output(){
        OutputWriter.writeln(lexicalities.get(0).toString());
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (lexicalitySupporter.isEmpty()) {
            return false;
        }
        if (lexicalitySupporter.read().getType().equals("INTTK")) {
            return true;
        }
        return false;
    }
}
