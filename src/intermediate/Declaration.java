package intermediate;

import backend.Mips;
import backend.SymbolManager;
import syntax.Variable;

import java.util.ArrayList;

public class Declaration extends IntermediateCode {
    public Declaration(Variable variable) {
        this.isConst = variable.isConst();
        this.name = variable.getFinalName();
        this.isGlobal = variable.isGlobal();
        this.space = variable.getLength();
        this.isArray = variable.getDimension() != 0;
        this.type = "Declaration";
        if (variable.isGlobal() || variable.isConst()) {
            ArrayList<Value> res = new ArrayList<>();
            for (int i : variable.getValues()) res.add(new Value(i));
            setValues(res);
        }
    }

    //space name isGlobal values
    private boolean isArray;
    private int space;
    private String name;
    private boolean isGlobal;
    private boolean isConst;
    private ArrayList<Value> values;

    public void solve() {
        Value left=new Value(0);
        if (isArray) {

        } else {
            left = new Value(name);
        }
        if (!isGlobal && values != null) {
            for (Value value : values) {
                SymbolManager.loadValueToRegister(value, "$t5");
                SymbolManager.storeValueFromRegister(left, "$t5");
            }
        }
    }

    public boolean isArray() {
        return isArray;
    }

    public String toString() {
        return String.format("%s %s %s[%d] %s", isGlobal() ? "global" : "local",
                isConst() ? "const" : "var", getName(), getSpace(), values == null ? "not init" : "init");
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public boolean isConst() {
        return isConst;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Value> getValues() {
        return values;
    }

    public void setValues(ArrayList<Value> values) {
        this.values = values;
    }

    public int getSpace() {
        return space;
    }
}
