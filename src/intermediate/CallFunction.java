package intermediate;

import backend.Mips;
import backend.SymbolManager;

public class CallFunction extends IntermediateCode{
    String name;
    Value value;
    public CallFunction(String name,Value value){
        this.name=name;
        this.type="CallFunction";
        this.value=value;
    }
    public String toString(){
        return String.format("call %s",name);
    }
    public Value getValue(){
        return value;
    }
    public void solve(){
        Mips.resetParamOffset();
        Mips.writeln(String.format("jal %s_function",name));
        Mips.writeln("nop");
        Mips.writeln("nop");
        SymbolManager.storeValueFromRegister(value,"$v0");
    }
}
