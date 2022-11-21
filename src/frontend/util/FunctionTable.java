package frontend.util;

import java.util.ArrayList;

public class FunctionTable {
    private FunctionTable() {
        functions = new ArrayList<>();
    }

    private static FunctionTable functionTable;

    public static FunctionTable getInstance() {
        if (functionTable == null) functionTable = new FunctionTable();
        return functionTable;
    }

    private ArrayList<Function> functions;

    public boolean isExist(String name) {
        for (Function function : functions) {
            if (name.equals(function.getName()))
                return true;
        }
        return false;
    }

    public void add(Function function) {
        if (functions == null)
            functions = new ArrayList<>();
        functions.add(function);
    }

    public Function get(String name) {
        for (Function function : functions) {
            if (name.equals(function.getName()))
                return function;
        }
        return null;
    }
}
