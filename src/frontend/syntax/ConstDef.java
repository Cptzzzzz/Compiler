package frontend.syntax;

import frontend.lexical.Lexicality;
import frontend.util.LexicalitySupporter;
import frontend.util.ParserUnit;
import frontend.util.Symbol;
import frontend.util.SymbolTable;
import midend.ir.Declaration;
import midend.util.IRSupporter;
import midend.util.Value;
import util.ErrorWriter;
import frontend.util.Node;

import java.util.ArrayList;

public class ConstDef extends ParserUnit {
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
        String name = getNode(0).getContent();
        ArrayList<Integer> dimension = new ArrayList<>();
        for (Node node : nodes) {
            if (node instanceof ConstExp)
                dimension.add(((ConstExp) node).getInteger());
        }
        if (SymbolTable.getInstance().isExist(name))
            ErrorWriter.add(getNode(0).getLineNumber(), 'b');
        else
            SymbolTable.getInstance().add(new Symbol(name, true, false, dimension, ((ConstInitVal) getNode(nodes.size() - 1)).getIntegers()));
    }

    @Override
    public Value generateIR() {
        Symbol symbol = SymbolTable.getInstance().getSymbol(getNode(0).getContent(), state.getBlockNumber());
        IRSupporter.getInstance().addIRCode(new Declaration(
                symbol.getFinalName(), state.getBlockNumber() == 0, true, symbol.getSize(), symbol.isReference(), symbol.getType(),
                ((ConstInitVal) getNode(nodes.size() - 1)).getIntegers()
        ));
        return null;
    }
}
