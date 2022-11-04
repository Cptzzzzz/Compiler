package intermediate;

public class Return extends IntermediateCode {
    Value value;

    public Return() {
        this.type="Return";
    }

    public Return(Value value) {
        this.value = value;
        this.type="Return";
    }

    public String toString() {
        if (value == null) return String.format("ret");
        else return String.format("ret %s", value);
    }
}
