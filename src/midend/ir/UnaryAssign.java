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

    public Operator getOperator() {
        return operator;
    }

    public Value getLeft() {
        return left;
    }

    public Value getRight() {
        return right;
    }
    public void setRight(Value right) {
        this.right = right;
    }
}
