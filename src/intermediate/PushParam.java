package intermediate;

public class PushParam extends IntermediateCode{
    Value value;
    public PushParam(Value value){
        this.value=value;
        this.type="PushParam";
    }
    public String toString(){
        return String.format("push %s",value.toString());
    }
}
