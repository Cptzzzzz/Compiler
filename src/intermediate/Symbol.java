package intermediate;

import syntax.Variable;

public class Symbol {
    private String name;
    private int type;//0: variable 1: array
    private boolean isReference;//for array ,judge whether reference
    private int space;
    public Symbol(String name,int type,boolean isReference,int space){
        this.name=name;
        this.type=type;
        this.isReference=isReference;
        this.space=space;
    }
    public boolean isVariable() {
        return type == 0;
    }

    public boolean isReference() {
        return isReference;
    }

    public Symbol(Variable variable, boolean isReference) {
        name = variable.getFinalName();
        space = 4;
        if (variable.getDimension() == 0) type = 0;
        else {
            type = 1;
            this.isReference = isReference;
            if (!isReference) space = variable.getLength() * 4;
        }
    }

    public Symbol(Declaration declaration) {
        this.name=declaration.getName();
        this.space=declaration.getValues().size()*4;
        this.type=declaration.isArray()?1:0;
        isReference=false;
    }

    public int getSpace() {
        return space;
    }

    public String getName() {
        return name;
    }
}
