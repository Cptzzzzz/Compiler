package midend.ir;

import midend.util.Operator;
import midend.util.Value;

public class UnaryAssign extends IRCode {
    private Value left;
    private Value right;
    private Operator operator;

    public UnaryAssign(Value left, Value right, Operator operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public String toString() {
        return String.format("%s = %s %s", left, operator, right);
    }
}
