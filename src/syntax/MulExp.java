package syntax;

import intermediate.Allocator;
import intermediate.Assign;
import intermediate.IntermediateCode;
import intermediate.Value;
import lexical.Lexicality;
import lexical.LexicalitySupporter;

import java.util.ArrayList;

public class MulExp extends ParserUnit {
    MulExp() {
        type ="MulExp";
    }

    public static MulExp parser(LexicalitySupporter lexicalitySupporter) {
        MulExp mulExp, temp;
        UnaryExp unaryExp;
        ArrayList<UnaryExp> arrayList = new ArrayList<>();
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        arrayList.add(UnaryExp.parser(lexicalitySupporter));
        while (lexicalitySupporter.read().getType().equals("MULT") ||
                lexicalitySupporter.read().getType().equals("DIV") ||
                lexicalitySupporter.read().getType().equals("MOD")) {
            lexicalities.add(lexicalitySupporter.readAndNext());
                arrayList.add(UnaryExp.parser(lexicalitySupporter));
        }
        int length=lexicalities.size();
        unaryExp = arrayList.get(0);
        mulExp=new MulExp();
        mulExp.add(unaryExp);
        for(int i=0;i<length;i++){
            temp=new MulExp();
            temp.add(mulExp);
            temp.add(lexicalities.get(i));
            temp.add(arrayList.get(i+1));
            mulExp=temp;
        }
        return mulExp;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (UnaryExp.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }

    public int getValue(){
        if(nodes.size()==1){
            return ((UnaryExp)nodes.get(0)).getValue();
        }else{
            if(nodes.get(1).getType().equals("MULT")){
                return ((MulExp)nodes.get(0)).getValue()*((UnaryExp)nodes.get(2)).getValue();
            }else if(nodes.get(1).getType().equals("DIV")){
                return ((MulExp)nodes.get(0)).getValue()/((UnaryExp)nodes.get(2)).getValue();
            }else{
                return ((MulExp)nodes.get(0)).getValue()%((UnaryExp)nodes.get(2)).getValue();
            }
        }
    }

    public String generateIntermediateCode() {
        if(nodes.size()==1){
            return ((UnaryExp)nodes.get(0)).generateIntermediateCode();
        }else{
            String v1=((MulExp)nodes.get(0)).generateIntermediateCode();
            String v2=((UnaryExp)nodes.get(2)).generateIntermediateCode();
            if(v1.matches("^(0|[1-9][0-9]*)$")&&v2.matches("^(0|[1-9][0-9]*)$")){
                if(nodes.get(1).getType().equals("MULT")){
                    return String.format("%d",
                            Integer.valueOf(v1)*Integer.valueOf(v2));
                }else if(nodes.get(1).getType().equals("DIV")){
                    return String.format("%d",
                            Integer.valueOf(v1)/Integer.valueOf(v2));
                }else{
                    return String.format("%d",
                            Integer.valueOf(v1)%Integer.valueOf(v2));
                }
            }
            String temp= Allocator.generateVariableName();
            IntermediateCode.add(new Assign(new Value(temp),
                     Assign.toNumber(((Lexicality)nodes.get(1)).getContent()),
                    new Value(v1),new Value(v2)));
            return temp;
        }
    }
}
