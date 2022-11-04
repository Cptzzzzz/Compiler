package syntax;

import intermediate.Allocator;
import intermediate.Assign;
import intermediate.IntermediateCode;
import intermediate.Value;
import lexical.LexicalitySupporter;

public class PrimaryExp extends ParserUnit {
    PrimaryExp() {
        type = "PrimaryExp";
    }

    public static PrimaryExp parser(LexicalitySupporter lexicalitySupporter) {
        PrimaryExp primaryExp = new PrimaryExp();
        if (lexicalitySupporter.read().getType().equals("LPARENT")) {
            primaryExp.add(lexicalitySupporter.readAndNext());
            primaryExp.add(Exp.parser(lexicalitySupporter));
            primaryExp.add(lexicalitySupporter.readAndNext());
        } else if (LVal.pretreat(lexicalitySupporter)) {
            primaryExp.add(LVal.parser(lexicalitySupporter));
        } else if (Number.pretreat(lexicalitySupporter)) {
            primaryExp.add(Number.parser(lexicalitySupporter));
        }
        return primaryExp;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (lexicalitySupporter.read().getType().equals("LPARENT")) {
            return true;
        } else if (LVal.pretreat(lexicalitySupporter)) {
            return true;
        } else if (Number.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }

    public int getValue() {
        if (nodes.get(0).getType().equals("LPARENT")) {
            return ((Exp) nodes.get(1)).getValue();
        } else if (nodes.get(0).getType().equals("Number")) {
            return ((Number) nodes.get(0)).getValue();
        } else {
            return ((LVal) nodes.get(0)).getValue();
        }
    }

    public String generateIntermediateCode() {
        if (nodes.get(0) instanceof Number) {
            return ((Number) nodes.get(0)).generateIntermediateCode();
        } else if (nodes.get(0) instanceof LVal) {
            Value value = Value.parser(((LVal) nodes.get(0)).generateIntermediateCode());
            if (value.getType() == Value.ARRAY) {
                String t = Allocator.generateVariableName();
                IntermediateCode.add(new Assign(
                        new Value(t), Assign.NONE, value
                ));
                return t;
            }
            return value.toString();
        } else {
            return ((Exp) nodes.get(1)).generateIntermediateCode();
        }
    }
}
