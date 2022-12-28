package frontend.syntax;

import frontend.lexical.Lexicality;
import frontend.util.*;
import midend.ir.Declaration;
import midend.ir.GetInt;
import midend.ir.UnaryAssign;
import midend.util.IRSupporter;
import midend.util.Operator;
import midend.util.Value;
import midend.util.ValueType;
import util.ErrorWriter;

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
            if (InitVal.pretreat(lexicalitySupporter))
                varDef.add(InitVal.parser(lexicalitySupporter));
            else if (lexicalitySupporter.read().getType().equals("GETINTTK")) {
                varDef.add(lexicalitySupporter.readAndNext());
                varDef.add(lexicalitySupporter.readAndNext());
                varDef.add(lexicalitySupporter.readAndNext());
            }
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
        super.semantic();
    }

    @Override
    public Value generateIR() {
        Symbol symbol = SymbolTable.getInstance().getSymbol(getNode(0).getContent(), state.getBlockNumber());
        ArrayList<Integer> values = null;
        if (state.getBlockNumber() == 0) {
            if (getNode(nodes.size() - 1) instanceof InitVal)
                values = ((InitVal) getNode(nodes.size() - 1)).getIntegers();
            else {
                values = new ArrayList<>();
                for (int i = symbol.getSize(); i > 0; i -= 4) {
                    values.add(0);
                }
            }
        }
        IRSupporter.getInstance().addIRCode(new Declaration(symbol.getFinalName(), state.getBlockNumber() == 0, false, symbol.getSize(), false, symbol.getType(), values));
        if (nodes.size() >= 3 && getNode(nodes.size() - 3).getType().equals("GETINTTK")) {
            IRSupporter.getInstance().addIRCode(new GetInt(new Value(symbol.getFinalName())));
        } else if (state.getBlockNumber() != 0 && getNode(nodes.size() - 1) instanceof InitVal) {
            ArrayList<Value> initValues = ((InitVal) getNode(nodes.size() - 1)).getValues();
            if (symbol.getType() == ValueType.Variable) {
                IRSupporter.getInstance().addIRCode(new UnaryAssign(new Value(symbol.getFinalName()), initValues.get(0), Operator.PLUS));
            } else {
                int size = initValues.size();
                for (int i = 0; i < size; i++) {
                    IRSupporter.getInstance().addIRCode(new UnaryAssign(new Value(symbol.getFinalName(), new Value(4 * i), false), initValues.get(i), Operator.PLUS));
                }
            }
        }
        return null;
    }
}
