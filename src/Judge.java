import backend.Mips;
import backend.SymbolManager;
import frontend.lexical.Lexicality;
import frontend.syntax.CompUnit;
import frontend.util.ContentScanner;
import frontend.util.FunctionTable;
import frontend.util.ParserUnit;
import frontend.util.SymbolTable;
import midend.util.IRSupporter;
import util.*;

import java.io.File;

public class Judge {
    private static boolean lexical;
    private static boolean syntax;
    private static boolean error;
    private static boolean mips;

    private static void init() {
        CompilerMode.getInstance().setLexical(false);
        CompilerMode.getInstance().setSyntax(true);
        CompilerMode.getInstance().setError(false);
        CompilerMode.getInstance().setIr(true);
        CompilerMode.getInstance().setMips(true);
    }

    public static void main(String[] args) {
        init();
        start();
    }

    public static void start() {
        System.out.println("Start judge");
        System.out.println(judge(2021, 'A', 1));

        int tot = 0, pass = 0;
        for (int year = 2021; year <= 2022; year++) {
            for (char type = 'A'; type <= 'C'; type++) {
                for (int number = 1; number <= 30; number++) {
                    if (judge(year, type, number)) {
                        pass++;
                        System.out.println("Pass: " + year + type + number);
                    } else {
                        System.out.println("Fail: " + year + type + number);
                    }
                    tot++;
                }
            }
        }
        if (tot == pass) {
            System.out.println("All passed!");
        } else {
            System.out.println("Passed " + pass + " / " + tot);
        }
    }


    public static boolean judge(int year, char type, int number) {
        Allocator.reset();
        IRSupporter.reset();
        FunctionTable.reset();
        SymbolTable.reset();
        SymbolManager.reset();
        String content = InputReader.readFile(String.format("data/%d/full/%c/testfile%d.txt", year, type, number));
        OutputWriter.init(String.format("output/syntax/%d/%c/result%d.txt", year, type, number));
        IRWriter.init(String.format("output/ir/%d/%c/result%d.txt", year, type, number));
        MipsWriter.init(String.format("output/mips/%d/%c/result%d.txt", year, type, number));
        Lexicality.init();
        content = ContentScanner.getInstance().pretreat(content);
        ContentScanner.getInstance().start(content);

        CompUnit root = ParserUnit.treeBuilder();
        root.output();

        root.passState();

        root.semantic();

        root.generateIR();
        IRSupporter.getInstance().output();

        Mips.generate();
        MipsWriter.close();
        IRWriter.close();
        OutputWriter.close();
/*String.format("java -jar ./Compiler_Mars.jar ./%s < ./%s | ./solve.exe > ./%s",
                    String.format("output/mips/%d/%c/result%d.txt", year, type, number),
                    String.format("data/%d/full/%c/input%d.txt", year, type, number),
                    String.format("output/output/%d/%c/result%d.txt", year, type, number))
                    ,"|","solve.exe",">",
                    String.format("./output/output/%d/%c/result%d.txt", year, type, number)*/
        try {
            AutoJudge.getInstance().createFile(String.format("output/output/%d/%c/result%d.txt", year, type, number));
            ProcessBuilder processBuilder = new ProcessBuilder("java","-jar","./Compiler_Mars.jar",
                    String.format("./output/mips/%d/%c/result%d.txt", year, type, number));
            processBuilder.redirectInput(new File(String.format("./data/%d/full/%c/input%d.txt", year, type, number)));
            processBuilder.redirectOutput(new File(String.format("./output/output/%d/%c/result%d.txt", year, type, number)));
            Process process = processBuilder.start();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return AutoJudge.getInstance().mipsCompare(String.format("data/%d/full/%c/output%d.txt", year, type, number), String.format("output/output/%d/%c/result%d.txt", year, type, number));
    }
}