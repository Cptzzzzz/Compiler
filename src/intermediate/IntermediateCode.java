package intermediate;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class IntermediateCode {
    private static BufferedWriter out;
    private static ArrayList<IntermediateCode> intermediateCodes;

    public static ArrayList<IntermediateCode> get() {
        return intermediateCodes;
    }

    public static void init() {
        intermediateCodes = new ArrayList<>();
        try {
            out = new BufferedWriter(new FileWriter("intermediate-code.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void add(IntermediateCode intermediateCode) {
        intermediateCodes.add(intermediateCode);
    }

    public static void output() {
        for (IntermediateCode intermediateCode : intermediateCodes) {
            writeln(intermediateCode.toString());
        }
    }

    public static void writeln(String string) {
        try {
            out.write(string + "\n");
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

    public String type;

    public static ArrayList<IntermediateCode> removeGlobal() {
        ArrayList<IntermediateCode> res = new ArrayList<>();
        boolean flag = false;
        for (IntermediateCode intermediateCode : intermediateCodes) {
            if (!(intermediateCode instanceof Declaration)) {
                flag = true;
            }
            if (flag) res.add(intermediateCode);
        }
        return res;
    }

    public void solve() {
    }
}
