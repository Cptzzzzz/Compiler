import lexical.ContentScanner;
import lexical.Lexicality;
import syntax.CompUnit;
import syntax.FunctionTable;
import syntax.ParserUnit;
import syntax.VariableTable;
import util.*;

public class Judge {
    public static void init() {
        CompilerMode.setDebug(false);
        CompilerMode.setStage("Error handling");
        CompilerMode.setJudge(false);
    }
    public static void main(String[] args) {
        Judge.init();
        judge();
    }
    public static void judge() {
        System.out.println("Begin test " + CompilerMode.getStage());
        System.out.println("------------------------------------------");
        if(CompilerMode.getStage().equals("Error handling")){
            error();
        }else if(CompilerMode.getStage().equals("Lexical analysis")||
                CompilerMode.getStage().equals("Syntax analysis")){
            analyse();
        }
    }

    public static void error(){
        int cnt=0,tot=0;
        for(int i=1;i<=13;i++){
            if(startError(i))cnt++;
            tot++;
        }
        System.out.println("------------------------------------------");
        System.out.println(String.format("You've passed %d of %d testcases.", cnt, tot));
        if (cnt == tot) {
            System.out.println("Congratulations!");
        } else {
            System.out.println("Please check your codes!");
        }
    }
    public static boolean startError(int number){
        String filename = String.format("data/Error/testfile/testfile%d.txt", number);
        String output = String.format("output/%s/2022/A/output%d.txt", CompilerMode.getStage(), number);
        String standard = String.format("data/Error/answer/error%d.txt",number);
        String content = InputReader.readFile(filename);
        AutoJudge.createFile(output);
        ErrorWriter.init(output);
        Lexicality.init();
        content = ContentScanner.pretreat(content);
        ContentScanner.start(content);
        CompUnit root = ParserUnit.treeBuilder();
        root.buildParent(null);
        root.buildFunctionTable(new FunctionTable());
        root.buildVariableTable(new VariableTable(1));
        root.setup();

        ErrorWriter.output();
        ErrorWriter.close();

        boolean res = AutoJudge.compare(standard, output);
        if (res) {
            System.out.println(String.format("testcase %d success",number));
        } else {
            System.out.println(String.format("testcase %d fail", number));
            AutoJudge.generateDiffFile(standard, output);
        }
        return res;
    }
    public static void analyse(){
        int cnt = 0, tot = 0;
        for (int year = 2021; year <= 2022; year++) {
            for (char c = 'A'; c <= 'C'; c++) {
                for (int number = 1; number <= 30; number++) {
                    if (startAnalyse(year, c, number)) {
                        cnt++;
                    }
                    tot++;
                }
            }
        }
        System.out.println("------------------------------------------");
        System.out.println(String.format("You've passed %d of %d testcases.", cnt, tot));
        if (cnt == tot) {
            System.out.println("Congratulations!");
        } else {
            System.out.println("Please check your codes!");
        }
    }
    public static boolean startAnalyse(int year, char c, int number) {
        String filename = String.format("data/%d/testfiles-only/%c/testfile%d.txt", year, c, number);
        String content = InputReader.readFile(filename);
        String output = String.format("output/%s/%d/%c/output%d.txt", CompilerMode.getStage(), year, c, number);
        String standard = String.format("data/%d/%s/%c/output%d.txt", year, CompilerMode.getStage(), c, number);

        AutoJudge.createFile(output);
        OutputWriter.init(output);
        Lexicality.init();
        content = ContentScanner.pretreat(content);
        ContentScanner.start(content);

        if (CompilerMode.getStage().equals("Lexical analysis"))
            Lexicality.outputAll();
        else if(CompilerMode.getStage().equals("Syntax analysis")){
            CompUnit root = ParserUnit.treeBuilder();
            root.output();
        }
        OutputWriter.close();

        boolean res = AutoJudge.compare(standard, output);
        if (res) {
            System.out.println(String.format("testcase %d-%c-%d success", year, c, number));
        } else {
            System.out.println(String.format("testcase %d-%c-%d fail", year, c, number));
            AutoJudge.generateDiffFile(standard, output);
        }
        return res;
    }
}
