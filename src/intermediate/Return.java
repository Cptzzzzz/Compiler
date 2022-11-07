package intermediate;

import backend.Mips;
import backend.SymbolManager;

public class Return extends IntermediateCode {
    Value value;

    public Return() {
        this.type="Return";
    }

    public Return(Value value) {
        this.value = value;
        this.type="Return";
    }

    public String toString() {
        if (value == null) return String.format("ret");
        else return String.format("ret %s", value);
    }

    public void solve(){
        if(value!=null){
            SymbolManager.loadValueToRegister(value,"$v0");
        }
        Mips.writeln(String.format("lw $ra,%d($sp)",SymbolManager.getLocalTableSpace()+4));
        Mips.writeln(String.format("addi $sp,$sp,%d",SymbolManager.getLocalTableSpace()+8));
        Mips.writeln("jr $ra");
        Mips.minusIndent();
    }
}
