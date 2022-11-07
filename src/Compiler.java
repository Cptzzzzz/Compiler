import backend.Mips;
import intermediate.Allocator;
import intermediate.IntermediateCode;
import lexical.ContentScanner;
import lexical.Lexicality;
import syntax.CompUnit;
import syntax.FunctionTable;
import syntax.ParserUnit;
import syntax.VariableTable;
import util.CompilerMode;
import util.ErrorWriter;
import util.InputReader;
import util.OutputWriter;

public class Compiler {
    public static void init() {
        CompilerMode.setDebug(false);
        CompilerMode.setStage("Code generate");
        CompilerMode.setJudge(false);
        IntermediateCode.init();
        OutputWriter.init("output.txt");
        ErrorWriter.init("error.txt");
        Lexicality.init();
    }

    public static void close() {
        OutputWriter.close();
        ErrorWriter.close();
        IntermediateCode.close();
    }

    public static void main(String[] args) {
        Compiler.init();
        submit();
        close();
    }

    public static void submit() {
        String content = InputReader.readFile("testfile.txt");
        content = ContentScanner.pretreat(content);
        ContentScanner.start(content);
        CompUnit root = ParserUnit.treeBuilder();
        root.buildParent(null);
        root.buildFunctionTable(new FunctionTable());
        root.buildVariableTable(new VariableTable(Allocator.generateTableNumber()));
        root.setup();//build FunctionTable and VariableTable
        root.generateIntermediateCode();

        Mips.start();
        if (CompilerMode.getStage().equals("Lexical analysis")) {
            Lexicality.outputAll();
        } else if (CompilerMode.getStage().equals("Syntax analysis")) {
            root.output();
        } else if (CompilerMode.getStage().equals("Error handling")) {
            ErrorWriter.output();
        } else if (CompilerMode.getStage().equals("Code generate")) {
            IntermediateCode.output();
        }
    }
}
