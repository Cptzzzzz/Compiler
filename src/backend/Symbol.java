package backend;

import midend.util.Value;
import midend.util.ValueType;

import java.util.HashMap;

public class Symbol {
    private final String name;
    private final ValueType type;
    private final int location;
    private final boolean reference;
    private int weight = 0;
    private HashMap<Value, Integer> weights = new HashMap<>();

    public void addWeight() {
            weight++;
//        System.out.println(name + " " + weight);
    }

    public int getWeight() {
        return weight;
    }
    public void addWeight(Value value) {
        if (weights.containsKey(value))
            weights.put(value, weights.get(value) + 1);
        else
            weights.put(value, 1);
    }
    public int getWeight(Value value) {
        return weights.getOrDefault(value, 0);
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
