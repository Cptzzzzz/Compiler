package intermediate;

public class GetInt extends IntermediateCode{
    Value left;
    public GetInt(Value value){
        left=value;
        this.type="GetInt";
    }
    public String toString(){
        return String.format("%s = getint()",left.toString());
    }
}
