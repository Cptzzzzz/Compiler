package syntax;

import lexical.LexicalitySupporter;

import java.util.ArrayList;

public class FuncFParams extends ParserUnit {
    FuncFParams() {
        type = "FuncFParams";
    }

    public static FuncFParams parser(LexicalitySupporter lexicalitySupporter) {
        FuncFParams funcFParams = new FuncFParams();
        funcFParams.add(FuncFParam.parser(lexicalitySupporter));
        while (lexicalitySupporter.read().getType().equals("COMMA")) {
            funcFParams.add(lexicalitySupporter.readAndNext());
            funcFParams.add(FuncFParam.parser(lexicalitySupporter));
        }
        return funcFParams;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (FuncFParam.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }
    public ArrayList<Variable> getParams(){
        int length=nodes.size();
        ArrayList<Variable> variables=new ArrayList<>();
        for(int i=0;i<length;i++){
            if(nodes.get(i) instanceof FuncFParam)
            variables.add(((FuncFParam) nodes.get(i)).getParam());
        }
        return variables;
    }
}
