package frontend.util;

import midend.util.ValueType;

import java.util.ArrayList;
import java.util.Arrays;

public class Symbol {
    private final String name;
    private final boolean isConst;
    private final boolean reference;
    private final int[] dimensions;
    private final ArrayList<Integer> values;
    private int scope;

    public ArrayList<Integer> getValues() {
        return values;
    }

    public Symbol(String name, boolean isConst, boolean reference, ArrayList<Integer> dimensions) {
        this.name = name;
        this.isConst = isConst;
        this.reference = reference;
        switch (dimensions.size()) {
            case 0:
                this.dimensions = null;
                break;
            case 1:
                this.dimensions = new int[1];
                this.dimensions[0] = dimensions.get(0);
                break;
            default:
                this.dimensions = new int[2];
                this.dimensions[0] = dimensions.get(0);
                this.dimensions[1] = dimensions.get(1);
        }
        this.values = null;
    }

    public Symbol(String name, boolean isConst, boolean reference, ArrayList<Integer> dimensions, ArrayList<Integer> values) {
        this.name = name;
        this.isConst = isConst;
        this.reference = reference;
        switch (dimensions.size()) {
            case 0:
                this.dimensions = null;
                break;
            case 1:
                this.dimensions = new int[1];
                this.dimensions[0] = dimensions.get(0);
                break;
            default:
                this.dimensions = new int[2];
                this.dimensions[0] = dimensions.get(0);
                this.dimensions[1] = dimensions.get(1);
        }
        this.values = values;
    }

    public boolean isReference() {
        return reference;
    }

    public String getName() {
        return name;
    }

    public int getScope() {
        return scope;
    }

    public void setScope(int scope) {
        this.scope = scope;
    }

    public boolean isConst() {
        return isConst;
    }

    public int getDimension() {
        if (dimensions == null) return 0;
        return dimensions.length;
    }

    public int[] getDimensions() {
        return dimensions;
    }

    public String getFinalName() {
        return String.format("%s_%d", name, scope);
    }

    public String toString() {
        return String.format("Name: %s, Scope: %d, Const: %b, Reference: %b, Dimension: %d, Dimensions: %s, Values: %s",
                name, scope, isConst, reference, getDimension(),
                dimensions == null ? "0" : Arrays.toString(dimensions),
                values == null ? "null" : values.toString());
    }

    public int getSize() {
        if (dimensions == null) return 4;
        if (reference) return 4;
        if (dimensions.length == 1) return 4 * dimensions[0];
        return 4 * dimensions[0] * dimensions[1];
    }

    public ValueType getType() {
        if (dimensions == null)
            return ValueType.Variable;
        return ValueType.Array;
    }
}
