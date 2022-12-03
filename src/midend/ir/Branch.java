package midend.ir;

import midend.util.Value;

public class Branch extends IRCode {
    private Value condition;
    private String label;
    private boolean isZeroBranch;

    public Branch(Value condition, String label, boolean isZeroBranch) {
        this.condition = condition;
        this.label = label;
        this.isZeroBranch = isZeroBranch;
    }

    public String toString() {
        return String.format("branch %s if %s %s 0", label, condition, isZeroBranch ? "==" : "!=");
    }

    public Value getCondition() {
        return condition;
    }

    public String getLabel() {
        return label;
    }

    public boolean isZeroBranch() {
        return isZeroBranch;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setCondition(Value value) {
        condition = value;
    }
}
