package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class OutputWriter {
    static BufferedWriter out;

    public static void init(String filename) {
        if (CompilerMode.getInstance().isLexical() || CompilerMode.getInstance().isSyntax())
            try {
                out = new BufferedWriter(new FileWriter(filename));
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public static void close() {
        if (CompilerMode.getInstance().isLexical() || CompilerMode.getInstance().isSyntax())
            try {
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public static void writeln(String string) {
        if (CompilerMode.getInstance().isLexical() || CompilerMode.getInstance().isSyntax())
            try {
                out.write(string + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
