import lexical.Lexicality;
import syntax.ParserUnit;

import java.util.ArrayList;

public class Test {
    public  void pr(){
        System.out.println("123");
    }
    public static void main(String[] args){
        ArrayList a=new ArrayList();
        a.add(new ParserUnit());
        a.add(new Lexicality());
        for(Object i:a){
            i.toString();
        }
    }
}

