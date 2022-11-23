package midend.ir;

public class Label extends IRCode {
    private String label;

    public Label(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return String.format("label: %s", label);
    }
}
