package syntax;

import intermediate.Value;
import lexical.LexicalitySupporter;
import util.Node;

import java.util.ArrayList;

public class InitVal extends ParserUnit {
    InitVal() {
        type = "InitVal";
    }

    public static InitVal parser(LexicalitySupporter lexicalitySupporter) {
        InitVal initVal = new InitVal();
        if (Exp.pretreat(lexicalitySupporter)) {
            initVal.add(Exp.parser(lexicalitySupporter));
        } else if (lexicalitySupporter.read().getType().equals("LBRACE")) {
            initVal.add(lexicalitySupporter.readAndNext());
            if (InitVal.pretreat(lexicalitySupporter)) {
                initVal.add(InitVal.parser(lexicalitySupporter));
                while (lexicalitySupporter.read().getType().equals("COMMA")) {
                    initVal.add(lexicalitySupporter.readAndNext());
                    initVal.add(InitVal.parser(lexicalitySupporter));
                }
            }
            initVal.add(lexicalitySupporter.readAndNext());
        }
        return initVal;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (Exp.pretreat(lexicalitySupporter)) {
            return true;
        }
        if (lexicalitySupporter.read().getType().equals("LBRACE")) {
            return true;
        }
        return false;
    }

    public ArrayList<Value> getValues(){
        ArrayList<Value> res=new ArrayList<>();
        for(Node node:nodes){
            if(node instanceof InitVal)
                for(Value value:((InitVal)node).getValues())
                    res.add(value);
            else if(node instanceof Exp)
                res.add(((Exp)node).generateIntermediateCode());
        }
        return res;
    }

    public ArrayList<Integer> getIntValues(){
        ArrayList<Integer> res=new ArrayList<>();
        for(Node node:nodes){
            if(node instanceof Exp){
                res.add(((Exp)node).getValue());
            }else if(node instanceof InitVal){
                for(int a:((InitVal)node).getIntValues()){
                    res.add(a);
                }
            }
        }
        return res;
    }
}
