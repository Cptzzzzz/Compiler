import util.CompilerMode;

public class Judge {
    private static boolean lexical;
    private static boolean syntax;
    private static boolean error;
    private static boolean mips;

    private static void init() {
        lexical = false;
        syntax = true;
        error = false;
        mips = false;
    }

    private static void compilerMode() {
        CompilerMode.getInstance().setLexical(lexical);
        CompilerMode.getInstance().setSyntax(syntax);
        CompilerMode.getInstance().setError(error);
        CompilerMode.getInstance().setMips(mips);
    }

    public static void main(String[] args) {
        init();
    }

    public static void lexicalJudge() {

    }
}
