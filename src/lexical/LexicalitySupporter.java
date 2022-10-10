package lexical;

public class LexicalitySupporter {
    int size;
    int pointer;

    public LexicalitySupporter(int size, int pointer) {
        this.size = size;
        this.pointer = pointer;
    }

    public static LexicalitySupporter builder() {
//        System.out.println(String.format("size: %d,pointer: %d\n",Lexicality.getSize(),0));
        return new LexicalitySupporter(Lexicality.getSize(), 0);
    }

    public boolean isEmpty() {
        return this.size == this.pointer;
    }

    public int size() {
        return this.size - this.pointer;
    }

    public boolean next() {
        if (pointer + 1 == size) return false;
        pointer++;
        return true;
    }

    public Lexicality read() {
        return Lexicality.get(pointer);
    }

    public void backspace(int length) {
        pointer -= length;
    }

    public String status() {
        return String.format("pointer:%d type:%s name:%s", pointer,
                Lexicality.get(pointer).getType(),
                Lexicality.get(pointer).getContent());
    }

    public int getPointer() {
        return pointer;
    }

    public void setPointer(int pointer) {
        this.pointer = pointer;
    }
}
