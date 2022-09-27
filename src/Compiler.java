import lexical.ContentScanner;
import lexical.Lexicality;
import util.CompilerMode;
import util.InputReader;
import util.OutputWriter;

public class Compiler {
    public static void init() {
        CompilerMode.setDebug(false);
        CompilerMode.setStage("Lexical analysis");
        CompilerMode.setJudge(false);
    }

    public static void main(String[] args) {
        Compiler.init();
        submit();
    }

    public static void submit() {
        String content = InputReader.readFile("testfile.txt");
        OutputWriter.init("output.txt");
        Lexicality.init();
        content = ContentScanner.pretreat(content);
        ContentScanner.start(content);

        if (CompilerMode.getStage().equals("Lexical analysis"))
            Lexicality.outputAll();
        OutputWriter.close();
    }

}
