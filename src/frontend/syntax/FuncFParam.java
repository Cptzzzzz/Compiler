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

public class FuncFParam extends ParserUnit {
    FuncFParam() {
        setType("FuncFParam");
    }

    public static FuncFParam parser(LexicalitySupporter lexicalitySupporter) {
        FuncFParam funcFParam = new FuncFParam();
        funcFParam.add(BType.parser(lexicalitySupporter));
        funcFParam.add(lexicalitySupporter.readAndNext());
        if (lexicalitySupporter.read().getType().equals("LBRACK")) {
            funcFParam.add(lexicalitySupporter.readAndNext());
            if (lexicalitySupporter.read().getType().equals("RBRACK")) {
                funcFParam.add(lexicalitySupporter.readAndNext());
                while (lexicalitySupporter.read().getType().equals("LBRACK")) {
                    funcFParam.add(lexicalitySupporter.readAndNext());
                    funcFParam.add(ConstExp.parser(lexicalitySupporter));
                    if (lexicalitySupporter.read().getType().equals("RBRACK")) {
                        funcFParam.add(lexicalitySupporter.readAndNext());
                    } else {
                        funcFParam.add(new Lexicality("]", "RBRACK"));
                        ErrorWriter.add(lexicalitySupporter.getLastLineNumber(), 'k');
                    }
                }
            } else {
                funcFParam.add(new Lexicality("]", "RBRACK"));
                ErrorWriter.add(lexicalitySupporter.getLastLineNumber(), 'k');
            }
        }
        return funcFParam;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return BType.pretreat(lexicalitySupporter);
    }

    public int getDimension() {
        int res = 0;
        for (Node node : nodes)
            if (node.getType().equals("LBRACK"))
                res++;
        return res;
    }

    public void semantic() {
        String name = getNode(1).getContent();
        ArrayList<Integer> dimensions = new ArrayList<>();
        if (SymbolTable.getInstance().isExist(name)) {
            ErrorWriter.add(getNode(1).getLineNumber(), 'b');
            return;
        }
        switch (nodes.size()) {
            case 2:
                SymbolTable.getInstance().add(new Symbol(name, false, false, dimensions));
                break;
            case 4:
                dimensions.add(0);
                SymbolTable.getInstance().add(new Symbol(name, false, true, dimensions));
                break;
            default:
                dimensions.add(0);
                dimensions.add(((ConstExp) getNode(5)).getInteger());
                SymbolTable.getInstance().add(new Symbol(name, false, true, dimensions));
                break;
        }
        state.setBlockNumber(SymbolTable.getInstance().getSymbol(name).getScope());
    }

    @Override
    public Value generateIR() {
        Symbol symbol = SymbolTable.getInstance().getSymbol(getNode(1).getContent(), state.getBlockNumber());
        IRSupporter.getInstance().addIRCode(new Declaration(symbol.getFinalName(), false, false, symbol.getSize(),
                symbol.isReference(), symbol.getType(), null));
        return null;
    }
}
