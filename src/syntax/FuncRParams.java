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

    public ArrayList<Variable> getParams(){
        ArrayList<Variable> variables=new ArrayList<>();
        for(Node node:nodes){
            if(node instanceof Exp){//todo 补全求参数维度的逻辑
                Variable variable=new Variable();
                ArrayList<Integer> dimensions=new ArrayList<>();
                for(int i=((Exp)node).getDimension();i>0;i--){
                    dimensions.add(0);
                }
                variable.setDimensions(dimensions);
                variables.add(variable);
            }
        }
        return variables;
    }
}
