package intermediate;

import backend.Mips;
import backend.SymbolManager;
import syntax.Variable;

import java.util.ArrayList;

public class Assign extends IntermediateCode {
    public static int NONE = 0;
    public static int PLUS = 1;
    public static int MINUS = 2;
    public static int MULTI = 3;
    public static int DIV = 4;
    public static int MOD = 5;
    public static int LSS = 6;
    public static int LEQ = 7;

    public static int toNumber(String operator) {
        if (operator.equals("*"))
            return MULTI;
        if (operator.equals("+"))
            return PLUS;
        if (operator.equals("-"))
            return MINUS;
        if (operator.equals("/"))
            return DIV;
        if (operator.equals("%"))
            return MOD;
        return NONE;
    }

    public static String toString(int operator) {
        switch (operator) {
            case 1:
                return "+";
            case 2:
                return "-";
            case 3:
                return "*";
            case 4:
                return "/";
            case 5:
                return "%";
            default:
                return "";
        }
    }

    int dimension;// 0:int 1:address 2:address
    Value left;
    ArrayList<Value> right;
    int operator;

    public Assign(Value leftValue, int operator, Value rightValue) {//数组读写或者单目运算符
        this.left = leftValue;
        this.right = new ArrayList<>();
        right.add(rightValue);
        this.operator = operator;
        this.type="Assign";
    }

    public Assign(Value leftValue, int operator, Value rightValue1, Value rightValue2) {//数组读写或者单目运算符
        this.left = leftValue;
        this.right = new ArrayList<>();
        right.add(rightValue1);
        right.add(rightValue2);
        this.operator = operator;
        this.type="Assign";
    }

    public String toString() {
        if (operator == Assign.NONE) {
            return String.format("%s = %s", left.toString(), right.get(0).toString());
        } else if (right.size() == 1) {
            return String.format("%s = %s %s", left.toString(), Assign.toString(operator), right.get(0).toString());
        } else {
            return String.format("%s = %s %s %s", left.toString(), right.get(0).toString(), Assign.toString(operator), right.get(1).toString());
        }
    }

    public void solve(){
        if(right.size()==1){
            if(operator==Assign.NONE){
                SymbolManager.loadValueToRegister(right.get(0),"$t0");
                SymbolManager.storeValueFromRegister(left,"$t0");
            }else if(operator==Assign.MINUS){
                SymbolManager.loadValueToRegister(right.get(0),"$t0");
                Mips.writeln("sub $t0,$zero,$t0");
                SymbolManager.storeValueFromRegister(left,"$t0");
            }
        }else{
            SymbolManager.loadValueToRegister(right.get(1),"$t1");
            Mips.writeln("sw $t1,0($sp)");
            SymbolManager.loadValueToRegister(right.get(0),"$t0");
            Mips.writeln("lw $t1,0($sp)");
            if(operator==Assign.MINUS){
                Mips.writeln("sub $t0,$t0,$t1");
            }else if(operator==Assign.PLUS){
                Mips.writeln("add $t0,$t0,$t1");
            }else if(operator==Assign.MULTI){
                Mips.writeln("mul $t0,$t0,$t1");
            }else if(operator==Assign.DIV){
                Mips.writeln("div $t0,$t0,$t1");
            }else if(operator==Assign.MOD){
                Mips.writeln("div $t0,$t1");
                Mips.writeln("mfhi $t0");
            }
            SymbolManager.storeValueFromRegister(left,"$t0");
        }
    }
    public Value getLeft(){
        return left;
    }
}
