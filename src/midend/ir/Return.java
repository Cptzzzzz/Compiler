package midend.ir;

import midend.util.Value;

public class Return extends IRCode {
    private int size;
    private Value value;
    public Return(Value value) {
        this.value = value;
    }

    @Override
    public String toString() {
        if(value==null)
            return "return";
        else
            return String.format("return %s", value);
    }
}
