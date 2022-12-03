package midend.ir;

import midend.util.Value;

public class PrintNumber extends IRCode{
    private Value value;
    public PrintNumber(Value value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("print number %s", value);
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }
}
