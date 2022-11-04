package syntax;

import lexical.LexicalitySupporter;
import util.Node;

import java.util.ArrayList;

public class ConstInitVal extends ParserUnit {
    ConstInitVal() {
        type = "ConstInitVal";
    }

    public static ConstInitVal parser(LexicalitySupporter lexicalitySupporter) {
        ConstInitVal constInitVal = new ConstInitVal();
        if (ConstExp.pretreat(lexicalitySupporter)) {
            constInitVal.add(ConstExp.parser(lexicalitySupporter));
        } else if (lexicalitySupporter.read().getType().equals("LBRACE")) {
            constInitVal.add(lexicalitySupporter.readAndNext());
            if (ConstInitVal.pretreat(lexicalitySupporter)) {
                constInitVal.add(ConstInitVal.parser(lexicalitySupporter));
                while (lexicalitySupporter.read().getType().equals("COMMA")) {
                    constInitVal.add(lexicalitySupporter.readAndNext());
                    constInitVal.add(ConstInitVal.parser(lexicalitySupporter));
                }
            }
            constInitVal.add(lexicalitySupporter.readAndNext());
        }
        return constInitVal;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (ConstExp.pretreat(lexicalitySupporter)) {
            return true;
        }
        if (lexicalitySupporter.read().getType().equals("LBRACE")) {
            return true;
        }
        return false;
    }

    public ArrayList<Integer> getValues(){
        ArrayList<Integer> res=new ArrayList<>();
        for(Node node:nodes){
            if(node instanceof ConstExp){
                res.add(((ConstExp)node).getValue());
            }else if(node instanceof ConstInitVal){
                for(int a:((ConstInitVal)node).getValues()){
                    res.add(a);
                }
            }
        }
        return res;
    }
}
