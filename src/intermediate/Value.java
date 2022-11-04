package intermediate;

import java.util.ArrayList;

public class Value {
    public static int NUMBER=0;
    public static int VARIABLE=1;
    public static int ARRAY=2;
    public static Value parser(String name){
        if(name.matches("^(0|[1-9][0-9]*)$")){
            return new Value(Integer.valueOf(name));
        }
        String[] tmp=name.split(" ");
        if(tmp.length==1){
            return new Value(name);
        }else{
            return new Value(tmp[0],new Value(tmp[1]));
        }
    }
    String name;
    Value dimension;
    int value;
    int type;

    public int getType() {
        return type;
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
    public Value(String name,Value dimension){
        this.name=name;
        this.dimension=dimension;
        this.type=Value.ARRAY;
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

    public String store(){
        if(type==Value.NUMBER){
            return String.format("%d",value);
        }else if(type==Value.VARIABLE){
            return name;
        }else{
            return String.format("%s %s",name,dimension.toString());
        }
    }
}
