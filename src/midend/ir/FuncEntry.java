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

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public String getName() {
        return name;
    }
}
