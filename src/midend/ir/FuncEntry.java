package midend.ir;

public class FuncEntry extends IRCode {
    private String name;
    private int size;
    public FuncEntry(String name) {
        this.name = name;
    }
    public String toString() {
        return String.format("function: %s", name);
    }
}
