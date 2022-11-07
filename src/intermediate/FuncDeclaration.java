package intermediate;

import backend.Mips;
import backend.SymbolManager;
import syntax.VariableTable;

public class FuncDeclaration extends IntermediateCode {
    public String name;
    public boolean isInt;
    int paramNumber;
    SymbolTable symbolTable;

    public FuncDeclaration(String name, boolean isInt, int paramNumber, VariableTable variableTable) {
        this.name = name;
        this.isInt = isInt;
        this.type = "FuncDeclaration";
        this.paramNumber = paramNumber;
        this.symbolTable = new SymbolTable(variableTable, paramNumber);
    }

    public String toString() {
        return String.format("%s %s()",
                isInt ? "int" : "void", name);
    }

    public void solve() {
        Mips.writeln("nop");
        Mips.writeln("nop");
        Mips.writeln(String.format("%s_function:", name));
        SymbolManager.setLocalTable(symbolTable);
        Mips.addIndent();
        Mips.writeln(String.format("addi $sp,$sp,-%d", symbolTable.getSpace() + 8));
        Mips.writeln(String.format("sw $ra,%d($sp)", symbolTable.getSpace() + 4));
    }

    public void addSymbol(Symbol symbol) {
        this.symbolTable.add(symbol);
    }
}
