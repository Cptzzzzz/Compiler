package midend.ir;

public class Jump extends IRCode{
    private String label;
    public Jump(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return String.format("jump %s", label);
    }

    public String getLabel() {
        return label;
    }
}
