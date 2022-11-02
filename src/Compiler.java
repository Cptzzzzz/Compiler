import lexical.ContentScanner;
import lexical.Lexicality;
import lexical.LexicalitySupporter;
import syntax.CompUnit;
import syntax.FunctionTable;
import syntax.ParserUnit;
import syntax.VariableTable;
import util.CompilerMode;
import util.Error;
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
        CompUnit root = ParserUnit.treeBuilder();
        root.buildParent(null);
        root.buildFunctionTable(new FunctionTable());
        root.buildVariableTable(new VariableTable());
        root.setup();

        if (CompilerMode.getStage().equals("Lexical analysis")) {
            Lexicality.outputAll();
        } else if (CompilerMode.getStage().equals("Syntax analysis")) {
            root.output();
        } else if (CompilerMode.getStage().equals("Error handling")) {
            ErrorWriter.output();
        }
        OutputWriter.close();
        ErrorWriter.close();
    }
}
