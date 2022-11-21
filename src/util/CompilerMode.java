package util;

public class CompilerMode {
    private static CompilerMode compilerMode;

    private CompilerMode() {
    }

    public static CompilerMode getInstance() {
        if (compilerMode == null)
            compilerMode = new CompilerMode();
        return compilerMode;
    }

    private boolean lexical;
    private boolean syntax;
    private boolean error;
    private boolean ir;
    private boolean mips;

    private boolean debug;

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isLexical() {
        return lexical;
    }

    public void setLexical(boolean lexical) {
        this.lexical = lexical;
    }

    public boolean isSyntax() {
        return syntax;
    }

    public void setSyntax(boolean syntax) {
        this.syntax = syntax;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isIr() {
        return ir;
    }

    public void setIr(boolean ir) {
        this.ir = ir;
    }

    public boolean isMips() {
        return mips;
    }

    public void setMips(boolean mips) {
        this.mips = mips;
    }
}
