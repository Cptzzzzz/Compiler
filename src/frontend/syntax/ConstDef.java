package frontend.syntax;

import frontend.lexical.Lexicality;
import frontend.lexical.LexicalitySupporter;
import frontend.util.Symbol;
import frontend.util.SymbolTable;
import util.ErrorWriter;
import util.Node;

import java.util.ArrayList;

public class ConstDef extends ParserUnit {
    private String symbol;

    ConstDef() {
        setType("ConstDef");
    }

    public static ConstDef parser(LexicalitySupporter lexicalitySupporter) {
        ConstDef constDef = new ConstDef();
        constDef.add(lexicalitySupporter.readAndNext());
        while (lexicalitySupporter.read().getType().equals("LBRACK")) {
            constDef.add(lexicalitySupporter.readAndNext());
            constDef.add(ConstExp.parser(lexicalitySupporter));
            if (lexicalitySupporter.read().getType().equals("RBRACK"))
                constDef.add(lexicalitySupporter.readAndNext());
            else {
                constDef.add(new Lexicality("]", "RBRACK"));
                ErrorWriter.add(lexicalitySupporter.getLastLineNumber(), 'k');
            }
        }
        if (lexicalitySupporter.read().getType().equals("ASSIGN")) {
            constDef.add(lexicalitySupporter.readAndNext());
            constDef.add(ConstInitVal.parser(lexicalitySupporter));
        }
        return constDef;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return lexicalitySupporter.read().getType().equals("IDENFR");
    }

    @Override
    public void semantic() {
        String name = nodes.get(0).getContent();
        ArrayList<Integer> dimension = new ArrayList<>();
        for (Node node : nodes) {
            if (node instanceof ConstExp)
                dimension.add(((ConstExp) node).getInteger());
        }
        if (SymbolTable.getInstance().isExist(name)) {
            ErrorWriter.add(nodes.get(0).getLineNumber(), 'b');
        } else {
            SymbolTable.getInstance().add(new Symbol(name, true, false, dimension, ((ConstInitVal) nodes.get(nodes.size() - 1)).getIntegers()));
            symbol = SymbolTable.getInstance().getSymbol(name).getFinalName();
        }
    }
}
