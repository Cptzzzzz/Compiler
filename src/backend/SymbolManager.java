package backend;

import intermediate.Symbol;
import intermediate.SymbolTable;
import intermediate.Value;

public class SymbolManager {
    private static SymbolTable globalTable;
    private static SymbolTable localTable;
    public static int getLocalTableSpace(){
        return localTable.getSpace();
    }
    public static void setLocalTable(SymbolTable symbolTable) {
        localTable = symbolTable;
    }

    public static void init() {
        globalTable = new SymbolTable();
    }

    public static void addGlobal(Symbol symbol) {
        globalTable.add(symbol);
    }

    public static void loadValueToRegister(Value value, String register) {
        if (value.isNumber()) {
            Mips.writeln(String.format("li %s,%d", register, value.getValue()));
        }else {
            if (value.isVariable()) {
                if (value.isGlobal()) {
                    globalTable.loadGlobalValue(value.getName(), false, register, "$zero");
                } else {
                    localTable.loadLocalValue(value.getName(), false, register, "$zero");
                }
            } else {
                loadValueToRegister(value.getDimension(), "$t6");
                if (value.isGlobal()) {
                    globalTable.loadGlobalValue(value.getName(), value.isLocation(), register, "$t6");
                } else {
                    localTable.loadLocalValue(value.getName(), value.isLocation(), register, "$t6");
                }
            }
        }
    }

    public static void storeValueFromRegister(Value value, String register) {
        if (value.isNumber()) {
            Mips.writeln(String.format("li %s,%d",register,value.getValue()));
        } else if (value.isVariable()) {
            if (value.isGlobal()) {
                globalTable.storeGlobalValue(value.getName(), register, "$zero");
            } else {
                localTable.storeLocalValue(value.getName(), register, "$zero");
            }
        } else {
            if (value.getDimension().isNumber()) {
                Mips.writeln(String.format("li $t6,%d", value.getDimension().getValue()));
            } else {
                loadValueToRegister(value.getDimension(), "$t6");
            }
            if (value.isGlobal()) {
                globalTable.storeGlobalValue(value.getName(), register, "$t6");
            } else {
                localTable.storeLocalValue(value.getName(), register, "$t6");
            }
        }
    }
}
