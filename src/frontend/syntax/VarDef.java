package frontend.syntax;

import frontend.lexical.Lexicality;
import frontend.util.LexicalitySupporter;
import frontend.util.ParserUnit;
import frontend.util.Symbol;
import frontend.util.SymbolTable;
import util.ErrorWriter;
import frontend.util.Node;

import java.util.ArrayList;

public class VarDef extends ParserUnit {
    VarDef() {
        setType("VarDef");
    }

    public static VarDef parser(LexicalitySupporter lexicalitySupporter) {
        VarDef varDef = new VarDef();
        varDef.add(lexicalitySupporter.readAndNext());
        while (lexicalitySupporter.read().getType().equals("LBRACK")) {
            varDef.add(lexicalitySupporter.readAndNext());
            varDef.add(ConstExp.parser(lexicalitySupporter));
            if (lexicalitySupporter.read().getType().equals("RBRACK"))
                varDef.add(lexicalitySupporter.readAndNext());
            else {
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
        if (lexicalitySupporter1.read().getType().equals("IDENFR"))
            lexicalitySupporter1.next();
        else
            return false;
        return !lexicalitySupporter1.read().getType().equals("LPARENT");
    }

    @Override
    public void semantic() {
        String name = getNode(0).getContent();
        ArrayList<Integer> dimension = new ArrayList<>();
        for (Node node : nodes)
            if (node instanceof ConstExp)
                dimension.add(((ConstExp) node).getInteger());
        if (SymbolTable.getInstance().isExist(name))
            ErrorWriter.add(getNode(0).getLineNumber(), 'b');
        else if (SymbolTable.getInstance().isGlobal() && getNode(nodes.size() - 1) instanceof InitVal)
            SymbolTable.getInstance().add(new Symbol(name, false, false, dimension, ((InitVal) getNode(nodes.size() - 1)).getIntegers()));
        else
            SymbolTable.getInstance().add(new Symbol(name, false, false, dimension));
    }
}
