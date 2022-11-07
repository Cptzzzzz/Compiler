package intermediate;

import backend.Mips;
import backend.SymbolManager;

public class GetInt extends IntermediateCode{
    Value left;
    public GetInt(Value value){
        left=value;
        this.type="GetInt";
    }
    public String toString(){
        return String.format("%s = getint()",left.toString());
    }
    public void solve(){
        Mips.writeln("li $v0,5");
        Mips.writeln("syscall");
        SymbolManager.storeValueFromRegister(left,"$v0");
    }
}
