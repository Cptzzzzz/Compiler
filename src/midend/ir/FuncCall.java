package midend.ir;

import midend.util.Value;

import java.util.ArrayList;

public class FuncCall extends IRCode {
    private ArrayList<Value> params;
    private String name;
    private Value result;

    public FuncCall(ArrayList<Value> params, String name, Value result) {
        this.params = params;
        this.name = name;
        this.result = result;
    }

    public String toString() {
        if (result == null)
            return String.format("call %s(%s)", name, params);
        else
            return String.format("%s = call %s(%s)", result, name, params);
    }
}
