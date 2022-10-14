package syntax;

import lexical.LexicalitySupporter;
import util.Node;

import java.util.ArrayList;

public class FuncRParams extends ParserUnit {
    FuncRParams() {
        type = "FuncRParams";
    }

    public static FuncRParams parser(LexicalitySupporter lexicalitySupporter) {
        FuncRParams funcRParams = new FuncRParams();
        funcRParams.add(Exp.parser(lexicalitySupporter));
        while (lexicalitySupporter.read().getType().equals("COMMA")) {
            funcRParams.add(lexicalitySupporter.readAndNext());
            funcRParams.add(Exp.parser(lexicalitySupporter));
        }
        return funcRParams;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (Exp.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }

    public int getParamNumber(){
        int res=0;
        for(Node node:nodes){
            if(node instanceof Exp){
                res++;
            }
        }
        return res;
    }

    public ArrayList<Integer> getParamDimensions(){
        ArrayList<Integer> res=new ArrayList<>();
        for(Node node:nodes){
            if(node instanceof Exp){
                res.add(((Exp) node).getDimension());
            }
        }
        return res;
    }
}
