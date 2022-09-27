import lexical.ContentScanner;
import lexical.Lexicality;
import util.InputReader;
import util.OutputWriter;

public class Compiler {
    public static void main(String[] args) {
        String content = InputReader.readFile("testfile.txt");
//        content=InputReader.readFile("data/testfiles-only/A/testfile1.txt");
        OutputWriter.init("output.txt");
        Lexicality.init();
        content = ContentScanner.pretreat(content);
        ContentScanner.start(content);

        Lexicality.outputAll();
        OutputWriter.close();
    }
}
