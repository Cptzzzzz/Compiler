package frontend.lexical;

public class LexicalitySupporter {
    int pointer;

    public LexicalitySupporter() {
        this.pointer = 0;
    }

    public LexicalitySupporter(int pointer) {
        this.pointer = pointer;
    }

    public static LexicalitySupporter builder() {
        return new LexicalitySupporter(0);
    }

    public void next() {
        pointer++;
    }

    public Lexicality read() {
        return Lexicality.get(pointer);
    }

    public Lexicality readAndNext() {
        pointer++;
        return Lexicality.get(pointer - 1);
    }

    public int getPointer() {
        return pointer;
    }

    public int getLastLineNumber() {
        return Lexicality.get(pointer - 1).getLineNumber();
    }

    public int getLineNumber() {
        return Lexicality.get(pointer).getLineNumber();
    }
}
