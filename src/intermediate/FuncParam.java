package intermediate;

import backend.Mips;
import backend.SymbolManager;

public class FuncParam extends IntermediateCode{
    public boolean isArray;
    public String name;
    public FuncParam(String name,boolean isArray){
        this.name=name;
        this.isArray=isArray;
        this.type="FuncParam";
    }
    public void solve(){

    }
    public String toString(){
        return String.format("para %s%s",name,isArray?"[]":"");
    }
}
