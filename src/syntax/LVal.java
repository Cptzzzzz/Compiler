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
        if(args.size()==0)
            args.add(0);
        return getVariableValue(((Lexicality) nodes.get(0)).getContent(), args);
    }

    public String generateIntermediateCode(){
        Variable variable=getVariableInstance(((Lexicality)nodes.get(0)).getContent());
        if(variable.getDimension()==0){
            if(variable.isConst()){
                return String.format("%d",variable.getValue(null));
            }else{
                return variable.getFinalName();
            }
        }else if(variable.getDimension()==1){
            String v=((Exp)nodes.get(2)).generateIntermediateCode();
            return new Value(variable.getFinalName(),new Value(v)).toString();
        }else{
            String v1=((Exp)nodes.get(2)).generateIntermediateCode();
            String v2=((Exp)nodes.get(5)).generateIntermediateCode();
            if(v1.matches("^(0|[1-9][0-9]*)$")){
                v1=String.format("%d",Integer.valueOf(v1)*variable.getDimensions().get(1));
            }else{
                String t=Allocator.generateVariableName();
                IntermediateCode.add(new Assign(
                        new Value(t),
                        Assign.MULTI,
                        new Value(v1),
                        new Value(variable.getDimensions().get(1))
                ));
                v1=t;
            }
            if(v2.matches("^(0|[1-9][0-9]*)$")&&v1.matches("^(0|[1-9][0-9]*)$")){
                v2=String.format("%d",Integer.valueOf(v1)+Integer.valueOf(v2));
            }else{
                String t=Allocator.generateVariableName();
                IntermediateCode.add(new Assign(
                        new Value(t),
                        Assign.PLUS,
                        new Value(v1),
                        new Value(v2)
                ));
                v2=t;
            }
            return new Value(variable.getFinalName(),new Value(v2)).store();
        }
    }
}

