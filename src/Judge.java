import lexical.ContentScanner;
import lexical.Lexicality;
import util.AutoJudge;
import util.CompilerMode;
import util.InputReader;
import util.OutputWriter;

import java.io.File;

public class Judge {
    public static void init() {
        CompilerMode.setDebug(false);
        CompilerMode.setStage("Lexical analysis");
        CompilerMode.setJudge(false);
    }
    public static void main(String[] args) {
        Compiler.init();
        judge();
    }
    public static void judge() {
        System.out.println("Begin test " + CompilerMode.getStage());
        System.out.println("------------------------------------------");
        int cnt = 0, tot = 0;
        for (int year = 2021; year <= 2022; year++) {
            for (char c = 'A'; c <= 'C'; c++) {
                for (int number = 1; number <= 30; number++) {
                    if (startJudge(year, c, number)) {
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

    public static boolean startJudge(int year, char c, int number) {
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
