package util;

public class Error {
    int line;
    char type;

    public Error(int line, char type) {
        this.line = line;
        this.type = type;
    }

    public int getLine() {
        return line;
    }

    public String toString(){
        return String.format("%d %c",line,type);
    }
}
