package intermediate;

public class FuncDeclaration extends IntermediateCode{
    public String name;
    public boolean isInt;
    public FuncDeclaration(String name,boolean isInt){
        this.name=name;
        this.isInt=isInt;
        this.type="FuncDeclaration";
    }

    public String toString(){
        return String.format("%s %s()",
                isInt?"int":"void",name);
    }
}
