import lexical.ContentScanner;
import lexical.Lexicality;
import lexical.LexicalitySupporter;
import syntax.CompUnit;
import syntax.ParserUnit;
import util.CompilerMode;
import util.ErrorWriter;
import util.InputReader;
import util.OutputWriter;

public class Compiler {
    public static void init() {
        CompilerMode.setDebug(false);
        CompilerMode.setStage("Syntax analysis");
        CompilerMode.setJudge(false);
    }

    public static void main(String[] args) {
        Compiler.init();
        submit();
    }

    public static void submit() {
        String content = InputReader.readFile("testfile.txt");
        OutputWriter.init("output.txt");
        ErrorWriter.init("error.txt");
        Lexicality.init();
        content = ContentScanner.pretreat(content);
        ContentScanner.start(content);
        LexicalitySupporter lexicalitySupporter = LexicalitySupporter.builder();
        CompUnit root = CompUnit.parser(lexicalitySupporter);

        if (CompilerMode.getStage().equals("Lexical analysis")) {
            Lexicality.outputAll();
        } else if (CompilerMode.getStage().equals("Syntax analysis")) {
            root.output();
        }
        OutputWriter.close();
        ErrorWriter.close();
    }
}
