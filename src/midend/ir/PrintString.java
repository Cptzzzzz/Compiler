package midend.ir;

import util.Allocator;

public class PrintString extends IRCode {
    private String content;
    private int number;

    public PrintString(String content) {
        this.content = content;
        this.number = Allocator.getInstance().getPrintStringNumber();
    }

    @Override
    public String toString() {
        return String.format("print string%d \"%s\"", number, content);
    }

    public String getContent() {
        return content;
    }

    public int getNumber() {
        return number;
    }
}
