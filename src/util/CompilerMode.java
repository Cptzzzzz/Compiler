package util;

public class CompilerMode {
    static boolean debug = true;

    public static boolean getDebug() {
        return debug;
    }

    public static void setDebug(boolean res) {
        debug = res;
    }

    static String stage;

    public static String getStage() {
        return stage;
    }

    public static void setStage(String res) {
        stage = res;
    }

    static boolean judge = false;

    public static void setJudge(boolean res) {
        judge = res;
    }

    public static boolean getJudge() {
        return judge;
    }
}
