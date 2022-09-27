package util;

public class CompilerMode {
    static boolean debug = true;

    public static boolean getDebug() {
        return debug;
    }
    public static void setDebug(boolean res){
        debug=res;
    }
    static String stage;

    public static String getStage() {
        return stage;
    }

    public static void setStage(String res){
        stage=res;
    }

    static String debugFilename;

    public static void setDebugFilename(int year,char category,int number){
        debugFilename=String.format("data/%d/testfiles-only/%c/testfile%d.txt",year,category,number);
    }
    public static String getDebugFilename(){
        return debugFilename;
    }
}
