package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Comparator;

public class ErrorWriter {
    private static BufferedWriter out;
    private static ArrayList<Error> errors;

    public static void init(String filename) {
        if (CompilerMode.getInstance().isError())
            try {
                errors = new ArrayList<>();
                out = new BufferedWriter(new FileWriter(filename));
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public static void close() {
        if (CompilerMode.getInstance().isError())
            try {
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public static void writeln(String string) {
        try {
            out.write(string + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void add(int line, char type) {
        if (CompilerMode.getInstance().isError())
            errors.add(new Error(line, type));
    }

    public static void output() {
        if (CompilerMode.getInstance().isError()) {
            errors.sort(Comparator.comparingInt(Error::getLine));
            for (Error error : errors) {
                writeln(error.toString());
            }
        }
    }
}
