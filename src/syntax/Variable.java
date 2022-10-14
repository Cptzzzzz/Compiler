package syntax;

import java.util.ArrayList;

public class Variable {
    boolean isConst;
    String name;
    ArrayList<Integer>  dimensions;
    public int getDimension(){
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
}
