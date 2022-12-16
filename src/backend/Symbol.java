package backend;

import midend.util.ValueType;

public class Symbol {
    private final String name;
    private final ValueType type;
    private final int location;
    private final boolean reference;
    private int weight = 0;

    public void addWeight() {
        weight++;
    }

    public int getWeight() {
        return weight;
    }

    public Symbol(String name, ValueType type) {//local
        this.name = name;
        this.type = type;
        this.location = -1;
        this.reference = false;
    }

    public Symbol(String name, ValueType type, int location, boolean reference) {//global
        this.name = name;
        this.type = type;
        this.location = location;
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public boolean isReference() {
        return reference;
    }

    public int getLocation() {
        return location;
    }
}
