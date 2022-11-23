package midend.ir;

import midend.util.ValueType;

import java.util.ArrayList;

public class Declaration extends IRCode {
    private String name;
    private boolean isGlobal;
    private boolean isConst;
    private int size;
    private boolean reference;
    private ValueType type;
    private ArrayList<Integer> values;

    public Declaration(String name, boolean isGlobal, boolean isConst, int size, boolean reference, ValueType type, ArrayList<Integer> values) {
        this.name = name;
        this.isGlobal = isGlobal;
        this.isConst = isConst;
        this.size = size;
        this.reference = reference;
        this.type = type;
        this.values = values;
    }

    public String toString() {
        return String.format("%s %s %s %s%s", isGlobal ? "global" : "local", isConst ? "const" : "var", type, reference ? "&" : "*", name);
    }
}
