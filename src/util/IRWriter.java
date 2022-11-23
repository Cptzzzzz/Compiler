package util;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class IRWriter {
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
}
