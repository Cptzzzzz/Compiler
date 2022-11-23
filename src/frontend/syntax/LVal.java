package frontend.syntax;

import frontend.lexical.Lexicality;
import frontend.util.LexicalitySupporter;
import frontend.util.ParserUnit;
import frontend.util.Symbol;
import frontend.util.SymbolTable;
import midend.ir.BinaryAssign;
import midend.util.IRSupporter;
import midend.util.Operator;
import midend.util.Value;
import midend.util.ValueType;
import util.Allocator;
import util.ErrorWriter;
import frontend.util.Node;

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
        if (lexicalitySupporter1.read().getType().equals("IDENFR"))
            lexicalitySupporter1.next();
        else
            return false;
        return !lexicalitySupporter1.read().getType().equals("LPARENT");
    }

    public int getInteger() {
        ArrayList<Integer> args = new ArrayList<>();
        for (Node node : nodes) {
            if (node instanceof Exp)
                args.add(((Exp) node).getInteger());
        }
        Symbol symbol = SymbolTable.getInstance().getSymbol(getNode(0).getContent());
        if (symbol == null) {
            ErrorWriter.add(getNode(0).getLineNumber(), 'c');
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
        String name = getNode(0).getContent();
        Symbol symbol = SymbolTable.getInstance().getSymbol(name);
        if (symbol == null)
            ErrorWriter.add(getNode(0).getLineNumber(), 'c');
        else
            state.setBlockNumber(symbol.getScope());
        super.semantic();
    }

    public int getDimension() {
        Symbol symbol = SymbolTable.getInstance().getSymbol(getNode(0).getContent());
        if (symbol == null) {
            ErrorWriter.add(getNode(0).getLineNumber(), 'c');
            return 0;
        }
        int cnt = 0;
        for (Node node : nodes)
            if (node instanceof Exp)
                cnt++;
        return symbol.getDimension() - cnt;
    }

    @Override
    public Value generateIR() {
        Symbol symbol = SymbolTable.getInstance().getSymbol(getNode(0).getContent(), state.getBlockNumber());
        ArrayList<Value> values = new ArrayList<>();
        for (Node node : nodes)
            if (node instanceof Exp)
                values.add(((Exp) node).generateIR());
        switch (symbol.getDimension()) {
            case 0:
                return new Value(symbol.getFinalName());
            case 1:
                if (values.size() == 0)
                    return new Value(symbol.getFinalName(), new Value(0), true);
                else {
                    Value value = values.get(0);
                    if (value.getType() == ValueType.Imm)
                        return new Value(symbol.getFinalName(), new Value(4 * value.getValue()), false);
                    Value value1 = Allocator.getInstance().getTemp();
                    IRSupporter.getInstance().addIRCode(new BinaryAssign(value1, value, new Value(4), Operator.MULTI));
                    return new Value(symbol.getFinalName(), value1, false);
                }
            default:
                switch (values.size()) {
                    case 0:
                        return new Value(symbol.getFinalName(), new Value(0), true);
                    case 1:
                        if (values.get(0).getType() == ValueType.Imm) {
                            return new Value(symbol.getFinalName(), new Value(4 * symbol.getDimensions()[1] * values.get(0).getValue()), true);
                        } else {
                            Value value = Allocator.getInstance().getTemp();
                            IRSupporter.getInstance().addIRCode(new BinaryAssign(
                                    value, values.get(0), new Value(4 * symbol.getDimensions()[1]), Operator.MULTI
                            ));
                            return new Value(symbol.getFinalName(), value, true);
                        }
                    default:
                        if (values.get(0).getType() == ValueType.Imm) {
                            if (values.get(1).getType() == ValueType.Imm) {
                                return new Value(symbol.getFinalName(),
                                        new Value(4 * symbol.getDimensions()[1] * values.get(0).getValue() + 4 * values.get(1).getValue()), false);
                            } else {
                                Value value = Allocator.getInstance().getTemp();
                                IRSupporter.getInstance().addIRCode(new BinaryAssign(
                                        value, values.get(1), new Value(4), Operator.MULTI
                                ));
                                Value value1 = Allocator.getInstance().getTemp();
                                IRSupporter.getInstance().addIRCode(new BinaryAssign(
                                        value1, value, new Value(4 * symbol.getDimensions()[1] * values.get(0).getValue()), Operator.PLUS
                                ));
                                return new Value(symbol.getFinalName(), value1, false);
                            }
                        } else {
                            Value value = Allocator.getInstance().getTemp();
                            IRSupporter.getInstance().addIRCode(new BinaryAssign(
                                    value, values.get(0), new Value(4 * symbol.getDimensions()[1]), Operator.MULTI
                            ));
                            Value value1 = Allocator.getInstance().getTemp();
                            if (values.get(1).getType() == ValueType.Imm) {
                                IRSupporter.getInstance().addIRCode(new BinaryAssign(
                                        value1, value, new Value(4 * values.get(1).getValue()), Operator.PLUS
                                ));
                                return new Value(symbol.getFinalName(), value1, false);
                            } else {
                                IRSupporter.getInstance().addIRCode(new BinaryAssign(
                                        value1, new Value(4), values.get(1), Operator.MULTI
                                ));
                                Value value2 = Allocator.getInstance().getTemp();
                                IRSupporter.getInstance().addIRCode(new BinaryAssign(
                                        value2, value, value1, Operator.PLUS
                                ));
                                return new Value(symbol.getFinalName(), value2, false);
                            }
                        }
                }
        }
    }
}
