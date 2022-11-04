package syntax;

import intermediate.Assign;
import intermediate.Declaration;
import intermediate.IntermediateCode;
import intermediate.Value;

import java.util.ArrayList;

public class Variable {
    boolean isConst;
    String name;
    ArrayList<Integer> dimensions;
    ArrayList<Integer> values;
    int tableNumber;

    public int getDimension() {
        return getDimensions().size();
    }

    public boolean isConst() {
        return isConst;
    }

    public void setConst(boolean aConst) {
        isConst = aConst;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Integer> getDimensions() {
        return dimensions;
    }

    public void setDimensions(ArrayList<Integer> dimensions) {
        this.dimensions = dimensions;
    }

    public ArrayList<Integer> getValues() {
        return values;
    }

    public void setValues(ArrayList<Integer> values) {
        this.values = values;
    }

    public int getValue(ArrayList<Integer> args) {
        if (getDimension() == 0) {
            return values.get(0);
        } else if (getDimension() == 1) {
            return values.get(args.get(0));
        } else {
            return values.get(args.get(0) * dimensions.get(1) + args.get(1));
        }
    }

    public String getFinalName() {
        return String.format("%s_%d", name, tableNumber);
    }

    public String toDeclarationString() {
        String res = "";
        if (isConst()) res += "const ";
        else res += "var ";
        if (getDimension() != 0)
            res += "arr ";
        else
            res += "num ";
        res += getFinalName();
        int dimension = 1;
        for (int dimension1 : dimensions) {
            dimension *= dimension1;
        }
        if (getDimension() > 0) res += String.format(" [%d]", dimension);
        return res;
    }

    public void generateIntermediateCode(int tableNumber) {
        this.tableNumber = tableNumber;
        IntermediateCode.add(new Declaration(this));
        if (isConst) {
            if (getDimension() == 0) {
                IntermediateCode.add(new Assign(
                        new Value(getFinalName()),Assign.NONE,new Value(values.get(0))
                ));
            } else if (getDimension() == 1) {
                for (int i = 0; i < dimensions.get(0); i++) {
                    IntermediateCode.add(new Assign(
                            new Value(getFinalName(), new Value(i)),
                            Assign.NONE,
                            new Value(values.get(i))
                    ));
                }
            } else if (getDimension() == 2) {
                int times = dimensions.get(1);
                for (int i = 0; i < dimensions.get(0); i++) {
                    for (int j = 0; j < dimensions.get(1); j++) {
                        IntermediateCode.add(new Assign(
                                new Value(getFinalName(), new Value(i * times + j)),
                                Assign.NONE,
                                new Value(values.get(i * times + j))
                        ));
                    }
                }
            }
        }
    }
}
