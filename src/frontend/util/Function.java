package frontend.util;

import java.util.ArrayList;

public class Function {
    private final String name;
    private final boolean value;// true: int   false:void
    private final ArrayList<Integer> dimensions;

    public Function(String name, boolean value, ArrayList<Integer> dimensions) {
        this.name = name;
        this.value = value;
        this.dimensions = dimensions;
    }

    public String getName() {
        return name;
    }

    public boolean isValue() {
        return value;
    }

    public ArrayList<Integer> getDimensions() {
        return dimensions;
    }

    public int getDimension() {
        return dimensions.size();
    }
}
