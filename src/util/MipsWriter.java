package util;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class MipsWriter {
    static BufferedWriter out;

    public static void init(String filename) {
        try {
            out = new BufferedWriter(new FileWriter(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void close() {
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
    public static void write(String string) {
        try {
            out.write(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
