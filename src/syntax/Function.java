package syntax;

import intermediate.FuncDeclaration;
import intermediate.FuncParam;
import intermediate.IntermediateCode;

import java.util.ArrayList;

public class Function {
    boolean returnValue;// 0 void  1 int
    String name;
    ArrayList<Variable> params;

    public boolean isReturnValue() {
        return returnValue;
    }

    public void setReturnValue(boolean returnValue) {
        this.returnValue = returnValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Variable> getParams() {
        return params;
    }

    public void setParams(ArrayList<Variable> params) {
        this.params = params;
    }

    public void generateIntermediateCode(){
        IntermediateCode.add(new FuncDeclaration(name,isReturnValue()));
        for(Variable variable:params){
            IntermediateCode.add(new FuncParam(variable.getFinalName(),variable.getDimension()!=0));
        }
    }
}
