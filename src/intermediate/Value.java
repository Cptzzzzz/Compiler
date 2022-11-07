package intermediate;

import java.util.ArrayList;

public class Value {
    public static int NUMBER=0;
    public static int VARIABLE=1;
    public static int ARRAY=2;
    boolean isLocation;//是否为取地址
    String name;//variable name for variable and array 变量名
    ArrayList<Integer> dimensions;// [3][5]  定义时数组大小
    Value dimension;//for array, [dimension][dimension] 实际调用时下标
    int value;//for number 整数值
    int type;//类型
    public boolean isTemp(){
        if(getType()==Value.VARIABLE){
            return name.matches("_temp\\d*");
        }
        return false;
    }

    public boolean isLocation() {
        return isLocation;
    }

    public Value(int value){
        this.value=value;
        this.type=Value.NUMBER;
    }
    public Value(String name){
        if(name.matches("^(0|[1-9][0-9]*)$")){
            this.type=Value.NUMBER;
            this.value=Integer.valueOf(name);
        }else{
            this.name=name;
            this.type=Value.VARIABLE;
        }
    }
    public Value(String name,Value dimension,boolean isLocation){
        this.name=name;
        this.dimension=dimension;
        this.type=Value.ARRAY;
        this.isLocation=isLocation;
    }
    public String toString(){
        if(type==Value.NUMBER){
            return String.format("%d",value);
        }else if(type==Value.VARIABLE){
            return name;
        }else{
            return String.format("%s[%s]",name,dimension.toString());
        }
    }
    public boolean isGlobal(){
        return name.matches(".*_1");
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Value getDimension() {
        return dimension;
    }

    public void setDimension(Value dimension) {
        this.dimension = dimension;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public boolean isNumber(){
        return type==Value.NUMBER;
    }
    public boolean isArray(){
        return type==Value.ARRAY;
    }
    public boolean isVariable(){
        return type==Value.VARIABLE;
    }
    public static void main(String[] args){
        System.out.println("_temp5".matches("_temp\\d"));
        System.out.println("a_temp5".matches("_temp\\d"));
        System.out.println("__temp5".matches("_temp\\d"));
    }
}
