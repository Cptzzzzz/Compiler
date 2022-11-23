package frontend.util;

import java.util.ArrayList;

public class SymbolTable {
    private SymbolTable() {
        symbols = new ArrayList<>();
        stack = new ArrayList<>();
        stack.add(0);
    }

    private static SymbolTable symbolTable;

    public static SymbolTable getInstance() {
        if (symbolTable == null)
            reset();
        return symbolTable;
    }

    public static void reset() {
        symbolTable = new SymbolTable();
    }

    public boolean isGlobal() {
        return getStackTop() == 0;
    }

    private final ArrayList<Symbol> symbols;
    private final ArrayList<Integer> stack;

    private int getStackTop() {
        return stack.get(stack.size() - 1);
    }

    public void push(int x) {
        if (x != getStackTop())
            stack.add(x);
    }

    public void pop() {
        stack.remove(stack.size() - 1);
    }

    public Symbol getSymbol(String name) {
        for (int i = symbols.size() - 1; i >= 0; i--) {
            Symbol symbol = symbols.get(i);
            if (stack.contains(symbol.getScope()) &&
                    name.equals(symbol.getName()))
                return symbol;
        }
        return null;
    }

    public Symbol getSymbol(String name, int scope) {
        for (int i = symbols.size() - 1; i >= 0; i--) {
            Symbol symbol = symbols.get(i);
            if (name.equals(symbol.getName()) &&
                    scope == symbol.getScope())
                return symbol;
        }
        return null;
    }

    public boolean isExist(String name) {
        int top = getStackTop();
        for (int i = symbols.size() - 1; i >= 0 && symbols.get(i).getScope() == top; i--)
            if (name.equals(symbols.get(i).getName()))
                return true;

        return false;
    }

    public void add(Symbol symbol) {
        symbol.setScope(getStackTop());
        symbols.add(symbol);
    }

    public void output() {
        for (Symbol symbol : symbols)
            System.out.println(symbol);
    }
}
