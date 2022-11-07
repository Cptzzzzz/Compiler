package syntax;

import intermediate.Allocator;
import intermediate.Assign;
import intermediate.IntermediateCode;
import intermediate.Value;
import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.Error;
import util.ErrorWriter;
import util.Node;

import java.util.ArrayList;
import java.util.Collection;

public class LVal extends ParserUnit {
    LVal() {
        type = "LVal";
    }

    public static LVal parser(LexicalitySupporter lexicalitySupporter) {
        LVal lVal = new LVal();
        lVal.add(lexicalitySupporter.readAndNext());
        while (lexicalitySupporter.read().getType().equals("LBRACK")) {
            lVal.add(lexicalitySupporter.readAndNext());
            lVal.add(Exp.parser(lexicalitySupporter));
            if (lexicalitySupporter.read().getType().equals("RBRACK")) {
                lVal.add(lexicalitySupporter.readAndNext());
            } else {
                lVal.add(new Lexicality("]", "RBRACK"));
                ErrorWriter.add(new Error(lexicalitySupporter.getLastLineNumber(), 'k'));
            }
        }
        return lVal;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        LexicalitySupporter lexicalitySupporter1 = new LexicalitySupporter(lexicalitySupporter.getPointer());
        if (lexicalitySupporter1.read().getType().equals("IDENFR")) {
            lexicalitySupporter1.next();
        } else {
            return false;
        }
        if (lexicalitySupporter1.read().getType().equals("LPARENT")) {
            return false;
        } else {
            return true;
        }
    }

    public void setup() {
        getVariable(getVariableName(), getVariableLineNumber(), false);
        super.setup();
    }

    public String getVariableName() {
        return ((Lexicality) nodes.get(0)).getContent();
    }

    public int getVariableLineNumber() {
        return ((Lexicality) nodes.get(0)).getLineNumber();
    }

    public int getDimension() {
        return getVariableDimension(getVariableName()) - (nodes.size() - 1) / 3;
    }

    public int getValue() {
        ArrayList<Integer> args = new ArrayList<>();
        for (Node node : nodes) {
            if (node instanceof Exp) {
                args.add(((Exp) node).getValue());
            }
        }
        if (args.size() == 0)
            args.add(0);
        return getVariableValue(((Lexicality) nodes.get(0)).getContent(), args);
    }

    public Value generateIntermediateCode() {
        Variable variable = getVariableInstance(((Lexicality) nodes.get(0)).getContent());
        if (variable.getDimension() == 0) {
            if (variable.isConst()) {
                return new Value(variable.getValue(null));
            } else {
                return new Value(variable.getFinalName());
            }
        } else if (variable.getDimension() == 1) {
            if (nodes.size() == 1) {
                return new Value(variable.getFinalName(), new Value(0), true);
            } else {
                Value temp = Allocator.generateVariableValue();
                IntermediateCode.add(new Assign(temp, Assign.MULTI, new Value(4), ((Exp) nodes.get(2)).generateIntermediateCode()));
                return new Value(variable.getFinalName(), temp, false);
            }
        } else {
            if (nodes.size() == 1) {
                return new Value(variable.getFinalName(), new Value(0), true);
            } else if (nodes.size() == 4) {
                Value temp = Allocator.generateVariableValue();
                IntermediateCode.add(new Assign(temp, Assign.MULTI, new Value(variable.dimensions.get(1)), ((Exp) nodes.get(2)).generateIntermediateCode()));
                Value temp1=Allocator.generateVariableValue();
                IntermediateCode.add(new Assign(temp1, Assign.MULTI, temp,new Value(4)));
                return new Value(variable.getFinalName(), temp1, true);
            } else {
                Value temp = Allocator.generateVariableValue();
                IntermediateCode.add(new Assign(temp, Assign.MULTI, new Value(variable.dimensions.get(1)), ((Exp) nodes.get(2)).generateIntermediateCode()));
                Value temp1 = Allocator.generateVariableValue();
                IntermediateCode.add(new Assign(temp1, Assign.PLUS, temp, ((Exp) nodes.get(5)).generateIntermediateCode()));
                Value temp2=Allocator.generateVariableValue();
                IntermediateCode.add(new Assign(temp2, Assign.MULTI,temp1,new Value(4)));
                return new Value(variable.getFinalName(),temp2, false);
            }
        }
    }
}

