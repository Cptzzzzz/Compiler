package midend.ir;

import midend.util.Operator;
import midend.util.Value;

public class BinaryAssign extends IRCode {
    private Value left;
    private Value[] right;
    private Operator operator;

    public BinaryAssign(Value left, Value right1, Value right2, Operator operator) {
        this.left = left;
        this.right = new Value[2];
        this.right[0] = right1;
        this.right[1] = right2;
        this.operator = operator;
    }

    public String toString() {
        return String.format("%s = %s %s %s", left, right[0], operator, right[1]);
    }

    public Value getLeft() {
        return left;
    }

    public Value getRight(int i) {
        return right[i];
    }

    public Operator getOperator() {
        return operator;
    }

    public void setLeft(Value left) {
        this.left = left;
    }

    public void setRight(int i, Value right) {
        this.right[i] = right;
    }
}
