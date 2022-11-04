package intermediate;

public class FuncParam extends IntermediateCode{
    public boolean isArray;
    public String name;
    public FuncParam(String name,boolean isArray){
        this.name=name;
        this.isArray=isArray;
        this.type="FuncParam";
    }

    public String toString(){
        return String.format("para %s%s",name,isArray?"[]":"");
    }
}
