package frontend.syntax;

import frontend.lexical.Lexicality;
import frontend.lexical.LexicalitySupporter;
import frontend.util.Symbol;
import frontend.util.SymbolTable;
import util.ErrorWriter;
import util.Node;

import java.util.ArrayList;

public class VarDef extends ParserUnit {
    private String symbol;

    VarDef() {
        setType("VarDef");
    }

    public static VarDef parser(LexicalitySupporter lexicalitySupporter) {
        VarDef varDef = new VarDef();
        varDef.add(lexicalitySupporter.readAndNext());
        while (lexicalitySupporter.read().getType().equals("LBRACK")) {
            varDef.add(lexicalitySupporter.readAndNext());
            varDef.add(ConstExp.parser(lexicalitySupporter));
            if (lexicalitySupporter.read().getType().equals("RBRACK")) {
                varDef.add(lexicalitySupporter.readAndNext());
            } else {
                varDef.add(new Lexicality("]", "RBRACK"));
                ErrorWriter.add(lexicalitySupporter.getLastLineNumber(), 'k');
            }
        }
        if (lexicalitySupporter.read().getType().equals("ASSIGN")) {
            varDef.add(lexicalitySupporter.readAndNext());
            varDef.add(InitVal.parser(lexicalitySupporter));
        }
        return varDef;
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
            if (SymbolTable.getInstance().isGlobal() && nodes.get(nodes.size() - 1) instanceof InitVal)
                SymbolTable.getInstance().add(new Symbol(name, false, false, dimension, ((InitVal) nodes.get(nodes.size() - 1)).getIntegers()));
            else
                SymbolTable.getInstance().add(new Symbol(name, false, false, dimension));
            symbol = SymbolTable.getInstance().getSymbol(name).getFinalName();
        }
    }
}
