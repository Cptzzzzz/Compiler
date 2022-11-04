package intermediate;

import java.util.ArrayList;

public class Printf extends IntermediateCode{
    String format;
    ArrayList<Value> param;

    public Printf(String format,ArrayList<Value> param){
        this.format=format;
        this.param=param;
        this.type="Printf";
    }

    public String toString(){
        return String.format("printf %s %s",format,param.toString());
    }
}
