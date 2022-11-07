package intermediate;

import backend.Mips;
import backend.SymbolManager;

import java.util.ArrayList;

public class Printf extends IntermediateCode {
    public static int time = 1;
    String format;
    ArrayList<Value> param;
    int number;

    public Printf(String format, ArrayList<Value> param) {
        this.format = format;
        this.param = param;
        this.type = "Printf";
        this.number = Printf.time;
        Printf.time++;
    }

    public void solve() {
        int pIndex=0,sIndex=0;
        int state=0;
        String format=this.format.substring(1, this.format.length() - 1);
        int length=format.length();
        for(int i=0;i<length;i++){
            boolean isString=true;
            if(i<length-1){
                String sub=format.substring(i,i+2);
                if(sub.equals("%d")){
                    SymbolManager.loadValueToRegister(param.get(pIndex),"$a0");
                    Mips.writeln("li $v0,1");
                    Mips.writeln("syscall");
                    state=0;
                    pIndex++;
                    i++;
                    isString=false;
                }
            }
            if(isString) if (state == 0) {
                Mips.writeln(String.format("la $a0,printf_str_%d_%d", number, sIndex));
                Mips.writeln("li $v0,4");
                Mips.writeln("syscall");
                state = 1;
                sIndex++;
            }
        }
    }

    public String toString() {
        return String.format("printf %s %s", format, param.toString());
    }

    public String[] getStrings() {
        return format.substring(1, format.length() - 1).split("%d");
    }

    public int getNumber() {
        return number;
    }

    public static void main(String[] args) {
        String a = "123456";
        System.out.println(a.substring(1, a.length() - 1));
    }
}
