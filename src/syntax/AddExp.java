package syntax;

import intermediate.Allocator;
import intermediate.Assign;
import intermediate.IntermediateCode;
import intermediate.Value;
import lexical.Lexicality;
import lexical.LexicalitySupporter;

import java.util.ArrayList;

public class AddExp extends ParserUnit {
    AddExp() {
        type = "AddExp";
    }

    public static AddExp parser(LexicalitySupporter lexicalitySupporter) {
        AddExp addExp, temp;
        MulExp mulExp;
        ArrayList<MulExp> arrayList = new ArrayList<>();
        ArrayList<Lexicality> lexicalities = new ArrayList<>();
        arrayList.add(MulExp.parser(lexicalitySupporter));
        while (lexicalitySupporter.read().getType().equals("PLUS") ||
                lexicalitySupporter.read().getType().equals("MINU")) {
            lexicalities.add(lexicalitySupporter.readAndNext());
            arrayList.add(MulExp.parser(lexicalitySupporter));
        }
        int length = lexicalities.size();
        mulExp = arrayList.get(0);
        addExp = new AddExp();
        addExp.add(mulExp);
        for (int i = 0; i < length; i++) {
            temp = new AddExp();
            temp.add(addExp);
            temp.add(lexicalities.get(i));
            temp.add(arrayList.get(i + 1));
            addExp = temp;
        }
        return addExp;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (MulExp.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }

    public int getValue() {
        if (nodes.size() == 1) {
            return ((MulExp) nodes.get(0)).getValue();
        } else {
            if (nodes.get(1).getType().equals("PLUS")) {
                return ((AddExp) nodes.get(0)).getValue() + ((MulExp) nodes.get(2)).getValue();
            } else {
                return ((AddExp) nodes.get(0)).getValue() - ((MulExp) nodes.get(2)).getValue();
            }
        }
    }

    public Value generateIntermediateCode() {
        if (nodes.size() == 1) {
            return ((MulExp) nodes.get(0)).generateIntermediateCode();
        } else {
            Value v1 = ((AddExp) nodes.get(0)).generateIntermediateCode();
            Value v2 = ((MulExp) nodes.get(2)).generateIntermediateCode();
            if (v1.isNumber()&&v2.isNumber()) {
                if (nodes.get(1).getType().equals("PLUS"))
                    return new Value(v1.getValue()+v2.getValue());
                return new Value(v1.getValue()-v2.getValue());
            }
            Value temp = Allocator.generateVariableValue();
            IntermediateCode.add(new Assign(temp,
                    nodes.get(1).getType().equals("PLUS") ? Assign.PLUS : Assign.MINUS,v1, v2));
            return temp;
        }
    }
}
