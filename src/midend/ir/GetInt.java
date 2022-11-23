package midend.ir;

import midend.util.Value;

public class GetInt extends IRCode{
    private Value value;
    public GetInt(Value value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%s = getint()", value);
    }
}
