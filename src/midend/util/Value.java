package midend.util;

public class Value {
    private final ValueType type;

    private int value;//if number, contains its value

    private String name;//if not number, contains its name
    private Value offset;
    private boolean address;//whether it is an address

    public Value(int value) {
        this.type = ValueType.Imm;
        this.value = value;
    }

    public Value(String name) {
        this.type = ValueType.Variable;
        this.name = name;
    }

    public Value(String name, Value offset, boolean address) {
        this.type = ValueType.Array;
        this.name = name;
        this.offset = offset;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public ValueType getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        switch (type) {
            case Imm:
                return String.valueOf(value);
            case Variable:
                return name;
            default:
                return address ? "&" : "*" + name + "[" + offset.toString() + "]";
        }
    }

    public Value getOffset() {
        return offset;
    }

    public boolean isAddress() {
        return address;
    }

    public void setOffset(Value offset) {
        this.offset = offset;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Value)) return false;
        Value value = (Value) obj;
        if (this.getType() != value.getType())
            return false;
        switch (type) {
            case Imm:
                return this.getValue() == value.getValue();
            case Variable:
                return this.getName().equals(value.getName());
            default:
                return false;
        }
    }

    @Override
    public int hashCode() {
        switch (type) {
            case Imm:
                return value;
            case Variable:
                return name.hashCode();
            default:
                return 0;
        }
    }
}
