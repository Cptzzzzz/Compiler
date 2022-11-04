package intermediate;

public class CallFunction extends IntermediateCode{
    String name;
    public CallFunction(String name){
        this.name=name;
        this.type="CallFunction";
    }
    public String toString(){
        return String.format("call %s",name);
    }
}
