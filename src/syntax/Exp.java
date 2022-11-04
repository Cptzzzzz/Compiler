package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.Node;

import java.util.ArrayList;

public class Exp extends ParserUnit {
    Exp() {
        type = "Exp";
    }

    public static Exp parser(LexicalitySupporter lexicalitySupporter) {
        Exp exp = new Exp();
        exp.add(AddExp.parser(lexicalitySupporter));
        return exp;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (AddExp.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }

    public int getDimension(){
        int res=0;
        for(Node node:collect("LVal")){
            if(((LVal)node).getDimension()>res){
                res=((LVal)node).getDimension();
            }
        }
        for(Node node:collect("UnaryExp")){
            if(node.nodes.get(0) instanceof Lexicality){
                Function function= functionTable.get(((Lexicality) node.nodes.get(0)).getContent());
                if(function!=null){
                    res=function.isReturnValue()?0:-1;
                }
            }
        }
        return res;
    }
    public int getValue(){
        return ((AddExp)nodes.get(0)).getValue();
    }

    public String generateIntermediateCode() {
        return ((AddExp)nodes.get(0)).generateIntermediateCode();
    }
}
