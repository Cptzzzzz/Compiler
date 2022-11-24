package backend;

import midend.util.Value;
import midend.util.ValueType;

import java.util.ArrayList;

public class SymbolManager {
    private static SymbolManager symbolManager;

    private SymbolManager() {

    }

    public static SymbolManager getInstance() {
        if (symbolManager == null)
            reset();
        return symbolManager;
    }

    public static void reset() {
        symbolManager = new SymbolManager();
    }

    private ArrayList<Symbol> localSymbols = new ArrayList<>();
    private ArrayList<Symbol> globalSymbols = new ArrayList<>();

    private boolean isGlobal(Value value) {
        return value.getName().matches(".*\\_0");
    }

    public void addSymbol(String name, ValueType type) {
        globalSymbols.add(new Symbol(name, type));
    }

    public void addSymbol(String name, ValueType type, int location, boolean reference) {
        localSymbols.add(new Symbol(name, type, location, reference));
    }

    public void load(Value value, String register) {
        if (value.getType() == ValueType.Imm) {
            Mips.writeln(String.format("li %s %d", register, value.getValue()));
            return;
        }
        if (isGlobal(value))
            loadGlobal(value, register);
        else
            loadLocal(value, register);
    }

    public void store(Value value, String register) {
        if (value.getType() == ValueType.Imm)
            return;
        if (isGlobal(value))
            storeGlobal(value, register);
        else
            storeLocal(value, register);
    }

    public void loadGlobal(Value value, String register) {
        if (value.getType() == ValueType.Variable) {
            Mips.writeln(String.format("lw %s,%s($zero)", register, value.getName()));
        } else {
            if (value.getOffset().getType() == ValueType.Imm) {
                if (value.isAddress())
                    Mips.writeln(String.format("la %s,%s+%d", register, value.getName(), value.getOffset().getValue()));
                else
                    Mips.writeln(String.format("lw %s,%s+%d", register, value.getName(), value.getOffset().getValue()));
            } else {
                load(value.getOffset(), "$t0");
                if (value.isAddress())
                    Mips.writeln(String.format("la %s,%s($t0)", register, value.getName()));
                else
                    Mips.writeln(String.format("lw %s,%s($t0)", register, value.getName()));
            }
        }
    }

    public void storeGlobal(Value value, String register) {
        if (value.getType() == ValueType.Variable) {
            Mips.writeln(String.format("sw %s,%s($zero)", register, value.getName()));
        } else {
            if (value.getOffset().getType() == ValueType.Imm) {
                Mips.writeln(String.format("sw %s,%s+%d", register, value.getName(), value.getOffset().getValue()));
            } else {
                load(value.getOffset(), "$t0");
                Mips.writeln(String.format("sw %s,%s($t0)", register, value.getName()));
            }
        }
    }

    public void loadLocal(Value value, String register) {
        Symbol symbol = getLocalSymbol(value.getName());
        assert symbol != null;
        if (value.getType() == ValueType.Variable) {
            Mips.writeln(String.format("lw %s,%d($sp)", register, symbol.getLocation()));
        } else {
            if (value.getOffset().getType() == ValueType.Imm) {
                if (value.isAddress()) {
                    if (symbol.isReference()) {
                        Mips.writeln(String.format("lw $t0,%d($sp)", symbol.getLocation()));
                        Mips.writeln(String.format("addu %s,$t0,%d", register, value.getOffset().getValue()));
                    } else {
                        Mips.writeln(String.format("addu %s,$sp,%d", register, value.getOffset().getValue() + symbol.getLocation()));
                    }
                } else {
                    if (symbol.isReference()) {
                        Mips.writeln(String.format("lw $t0,%d($sp)", symbol.getLocation()));
                        Mips.writeln(String.format("lw %s,%d($t0)", register, value.getOffset().getValue()));
                    } else {
                        Mips.writeln(String.format("lw %s,%d($sp)", register, value.getOffset().getValue() + symbol.getLocation()));
                    }
                }
            } else {
                load(value.getOffset(), "$t0");
                if (value.isAddress()) {
                    if (symbol.isReference()) {
                        Mips.writeln(String.format("lw $t1,%d($sp)", symbol.getLocation()));
                        Mips.writeln(String.format("addu %s,$t0,$t1", register));
                    } else {
                        Mips.writeln(String.format("addu $t0,$t0,%d", symbol.getLocation()));
                        Mips.writeln(String.format("addu %s,$t0,$sp", register));
                    }
                } else {
                    if (symbol.isReference()) {
                        Mips.writeln(String.format("lw $t1,%d($sp)", symbol.getLocation()));
                        Mips.writeln("addu $t0,$t0,$t1");
                        Mips.writeln(String.format("lw %s,0($t0)", register));
                    } else {
                        Mips.writeln("addu $t0,$t0,$sp");
                        Mips.writeln(String.format("lw %s,%d($t0)", register, symbol.getLocation()));
                    }
                }
            }
        }
    }


    public void storeLocal(Value value, String register) {
        Symbol symbol = getLocalSymbol(value.getName());
        assert symbol != null;
        if (value.getType() == ValueType.Variable) {
            Mips.writeln(String.format("sw %s,%d($sp)", register, symbol.getLocation()));
        } else {
            if (value.getOffset().getType() == ValueType.Imm) {
                if (symbol.isReference()) {
                    Mips.writeln(String.format("lw $t0,%d($sp)", symbol.getLocation()));
                    Mips.writeln(String.format("sw %s,%d($t0)", register, value.getOffset().getValue()));
                } else {
                    Mips.writeln(String.format("sw %s,%d($sp)", register, value.getOffset().getValue() + symbol.getLocation()));
                }
            } else {
                load(value.getOffset(), "$t0");
                if (symbol.isReference()) {
                    Mips.writeln(String.format("lw $t1,%d($sp)", symbol.getLocation()));
                    Mips.writeln("addu $t0,$t0,$t1");
                    Mips.writeln(String.format("sw %s,0($t0)", register));
                } else {
                    Mips.writeln("addu $t0,$t0,$sp");
                    Mips.writeln(String.format("sw %s,%d($t0)", register, symbol.getLocation()));
                }
            }
        }
    }

    private Symbol getLocalSymbol(String name) {
        for (Symbol symbol : localSymbols)
            if (symbol.getName().equals(name))
                return symbol;
        return null;
    }
}
