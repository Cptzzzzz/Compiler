package intermediate;

import backend.Mips;
import backend.SymbolManager;

public class PushParam extends IntermediateCode{
    Value value;
    public PushParam(Value value){
        this.value=value;
        this.type="PushParam";
    }
    public String toString(){
        return String.format("push %s",value.toString());
    }
    public void solve(){
        SymbolManager.loadValueToRegister(value,"$t3");
        Mips.writeln(String.format("sw $t3,%d($sp)",Mips.getParamOffset()));
    }
}
