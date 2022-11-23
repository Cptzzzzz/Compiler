package midend.ir;

public class FuncEnd extends IRCode {
    private String name;
    public FuncEnd(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return String.format("function %s end", name);
    }
}
