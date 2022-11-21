package frontend.syntax;

import frontend.lexical.Lexicality;
import frontend.lexical.LexicalitySupporter;
import frontend.util.Symbol;
import frontend.util.SymbolTable;
import util.ErrorWriter;
import util.Node;

import java.util.ArrayList;

public class LVal extends ParserUnit {
    LVal() {
        setType("LVal");
    }

    public static LVal parser(LexicalitySupporter lexicalitySupporter) {
        LVal lVal = new LVal();
        lVal.add(lexicalitySupporter.readAndNext());
        while (lexicalitySupporter.read().getType().equals("LBRACK")) {
            lVal.add(lexicalitySupporter.readAndNext());
            lVal.add(Exp.parser(lexicalitySupporter));
            if (lexicalitySupporter.read().getType().equals("RBRACK"))
                lVal.add(lexicalitySupporter.readAndNext());
            else {
                lVal.add(new Lexicality("]", "RBRACK"));
                ErrorWriter.add(lexicalitySupporter.getLastLineNumber(), 'k');
            }
        }
        return lVal;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        LexicalitySupporter lexicalitySupporter1 = new LexicalitySupporter(lexicalitySupporter.getPointer());
        if (lexicalitySupporter1.read().getType().equals("IDENFR")) {
            lexicalitySupporter1.next();
        } else {
            return false;
        }
        return !lexicalitySupporter1.read().getType().equals("LPARENT");
    }

    public int getInteger() {
        ArrayList<Integer> args = new ArrayList<>();
        for (Node node : nodes) {
            if (node instanceof Exp) {
                args.add(((Exp) node).getInteger());
            }
        }
        Symbol symbol = SymbolTable.getInstance().getSymbol(nodes.get(0).getContent());
        if (symbol == null) {
            ErrorWriter.add(nodes.get(0).getLineNumber(), 'c');
            return 0;
        }
        switch (symbol.getDimension()) {
            case 0:
                return symbol.getValues().get(0);
            case 1:
                return symbol.getValues().get(args.get(0));
            default:
                return symbol.getValues().get(args.get(1) + args.get(0) * symbol.getDimensions()[1]);
        }
    }

    public void semantic() {
        String name = nodes.get(0).getContent();
        if (SymbolTable.getInstance().getSymbol(name) == null)
            ErrorWriter.add(nodes.get(0).getLineNumber(), 'c');
    }

    public int getDimension() {
        Symbol symbol = SymbolTable.getInstance().getSymbol(nodes.get(0).getContent());
        if (symbol == null) {
            ErrorWriter.add(nodes.get(0).getLineNumber(), 'c');
            return 0;
        }
        int cnt = 0;
        for (Node node : nodes) {
            if (node instanceof Exp)
                cnt++;
        }
        return symbol.getDimension() - cnt;
    }
}
