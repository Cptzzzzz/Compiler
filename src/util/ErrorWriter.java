package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Comparator;

public class ErrorWriter {
    static BufferedWriter out;
    static ArrayList<Error> errors;

    public static void init(String filename) {
        try {
            out = new BufferedWriter(new FileWriter(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
        errors = new ArrayList<>();
    }

    public static void close() {
        try {
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void write(String string) {
        try {
            out.write(string);
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

    public static void output() {
        errors.sort(Comparator.comparingInt(Error::getLine));
        for (Error error : errors) {
            writeln(error.toString());
        }
    }

    public static void add(Error error) {
        errors.add(error);
    }
}
