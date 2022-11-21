import frontend.lexical.ContentScanner;
import frontend.lexical.Lexicality;
import frontend.syntax.CompUnit;
import frontend.syntax.ParserUnit;
import frontend.syntax.State;
import util.CompilerMode;
import util.ErrorWriter;
import util.InputReader;
import util.OutputWriter;

public class Compiler {
    public static void init() {
        CompilerMode.getInstance().setLexical(false);
        CompilerMode.getInstance().setSyntax(true);
        CompilerMode.getInstance().setError(false);
        CompilerMode.getInstance().setIr(false);
        CompilerMode.getInstance().setMips(false);
        CompilerMode.getInstance().setDebug(false);
    }

    public static void main(String[] args) {
        init();
        submit();
    }

    public static void submit() {
        String content = InputReader.readFile("testfile.txt");
        OutputWriter.init("output.txt");
        ErrorWriter.init("error.txt");
        Lexicality.init();


        content = ContentScanner.getInstance().pretreat(content);//预处理

        ContentScanner.getInstance().start(content);//词法分析
        Lexicality.outputAll();//输出词法分析

        CompUnit root = ParserUnit.treeBuilder();//语法分析+错误处理
        root.output();//输出语法分析

        root.setState(new State(0, 0, false, false));

        root.semantic();//语义分析+错误处理
        ErrorWriter.output();//输出错误处理结果

        OutputWriter.close();
        ErrorWriter.close();
    }
}
