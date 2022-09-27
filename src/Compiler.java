import lexical.ContentScanner;
import lexical.Lexicality;
import util.CompilerMode;
import util.InputReader;
import util.OutputWriter;

public class Compiler {
    public static void init() {
        CompilerMode.setDebug(true);
        CompilerMode.setStage("lexical");
        CompilerMode.setDebugFilename(2022, 'A', 1);
    }

    public static void main(String[] args) {
        Compiler.init();
        String content = InputReader.readFile("testfile.txt");
        if (CompilerMode.getDebug())
            content = InputReader.readFile(CompilerMode.getDebugFilename());
        OutputWriter.init("output.txt");
        Lexicality.init();
        content = ContentScanner.pretreat(content);
        ContentScanner.start(content);

        if (CompilerMode.getStage().equals("lexical"))
            Lexicality.outputAll();
        OutputWriter.close();
    }
}
