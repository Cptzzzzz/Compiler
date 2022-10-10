package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class RelExp extends ParserUnit {
    RelExp() {

    }

    RelExp(String name, ArrayList<ParserUnit> units) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
    }

    RelExp(String name, ArrayList<ParserUnit> units, ArrayList<Lexicality> lexicalities) {
        this.name = name;
        this.derivations = new ArrayList<>(units);
        this.lexicalities = new ArrayList<>(lexicalities);
    }

    public static RelExp parser(LexicalitySupporter lexicalitySupporter) {
        ArrayList<ParserUnit> arrayList = new ArrayList<>();
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        arrayList.add(AddExp.parser(lexicalitySupporter));
        while (lexicalitySupporter.read().getType().equals("LSS") ||
                lexicalitySupporter.read().getType().equals("LEQ") ||
                lexicalitySupporter.read().getType().equals("GRE") ||
                lexicalitySupporter.read().getType().equals("GEQ")) {
            lexicalities.add(lexicalitySupporter.read());
            lexicalitySupporter.next();
            if (AddExp.pretreat(lexicalitySupporter)) {
                arrayList.add(AddExp.parser(lexicalitySupporter));
            }
        }

        return new RelExp("RelExp", arrayList, lexicalities);
    }

    public void output() {
        derivations.get(0).output();
        int length = lexicalities.size();
        for (int i = 0; i < length; i++) {
            OutputWriter.writeln(String.format("<%s>", name));
            OutputWriter.writeln(lexicalities.get(i).toString());
            derivations.get(i + 1).output();
        }
        OutputWriter.writeln(String.format("<%s>", name));
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (lexicalitySupporter.isEmpty()) {
            return false;
        }
        if (AddExp.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }
}
