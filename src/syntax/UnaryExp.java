package syntax;

import intermediate.*;
import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.Error;
import util.ErrorWriter;
import util.Node;

import java.util.ArrayList;

public class UnaryExp extends ParserUnit {
    UnaryExp() {
        type = "UnaryExp";
    }

    public static UnaryExp parser(LexicalitySupporter lexicalitySupporter) {
        UnaryExp unaryExp = new UnaryExp();
        if (PrimaryExp.pretreat(lexicalitySupporter)) {
            unaryExp.add(PrimaryExp.parser(lexicalitySupporter));
        } else if (lexicalitySupporter.read().getType().equals("IDENFR")) {
            unaryExp.add(lexicalitySupporter.readAndNext());
            if (lexicalitySupporter.read().getType().equals("LPARENT")) {
                unaryExp.add(lexicalitySupporter.readAndNext());
                if (FuncRParams.pretreat(lexicalitySupporter)) {
                    unaryExp.add(FuncRParams.parser(lexicalitySupporter));
                }
                if (lexicalitySupporter.read().getType().equals("RPARENT")) {
                    unaryExp.add(lexicalitySupporter.readAndNext());
                } else {
                    unaryExp.add(new Lexicality(")", "RPARENT"));
                    ErrorWriter.add(new Error(lexicalitySupporter.getLastLineNumber(), 'j'));
                }
            }
        } else if (UnaryOp.pretreat(lexicalitySupporter)) {
            unaryExp.add(UnaryOp.parser(lexicalitySupporter));
            unaryExp.add(UnaryExp.parser(lexicalitySupporter));
        }
        return unaryExp;
    }


    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (PrimaryExp.pretreat(lexicalitySupporter)) {
            return true;
        } else if (lexicalitySupporter.read().getType().equals("IDENFR")) {
            return true;
        } else if (UnaryOp.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }

    public void setup() {
        if (nodes.get(0) instanceof Lexicality) {
            Function function = functionTable.get(((Lexicality) nodes.get(0)).getContent());
            if (function == null) {
                ErrorWriter.add(new Error(((Lexicality) nodes.get(0)).getLineNumber(), 'c'));
            } else {
                int number = getParamNumber();
                if (number != function.getParams().size()) {
                    ErrorWriter.add(new Error(((Lexicality) nodes.get(0)).getLineNumber(), 'd'));
                }
                for (int i = 0; i < number; i++) {
                    try {
                        if (getParamDimensions().get(i) != function.getParams().get(i).getDimension()) {
                            ErrorWriter.add(new Error(((Lexicality) nodes.get(0)).getLineNumber(), 'e'));
                            break;
                        }
                    } catch (Exception e) {

                    }
                }
            }
        }
        super.setup();
    }

    public int getParamNumber() {
        if (nodes.size() == 3)
            return 0;
        return ((FuncRParams) nodes.get(2)).getParamNumber();
    }

    public ArrayList<Integer> getParamDimensions() {
        if (getParamNumber() == 0) return new ArrayList<>();
        return ((FuncRParams) nodes.get(2)).getParamDimensions();
    }

    public int getValue() {
        if (nodes.get(0) instanceof UnaryOp) {
            return (nodes.get(0).nodes.get(0).getType().equals("PLUS") ? 1 : -1) * ((UnaryExp) nodes.get(1)).getValue();
        } else {
            return ((PrimaryExp) nodes.get(0)).getValue();
        }
    }

    public Value generateIntermediateCode() {
        if(nodes.get(0) instanceof UnaryOp){
            if(nodes.get(0).nodes.get(0).getType().equals("PLUS")){
                return ((UnaryExp)nodes.get(1)).generateIntermediateCode();
            }else if(nodes.get(0).nodes.get(0).getType().equals("MINU")){
                Value temp = Allocator.generateVariableValue();
                IntermediateCode.add(new Assign(temp,Assign.MINUS,((UnaryExp)nodes.get(1)).generateIntermediateCode()));
                return temp;
            }else{//todo implement !

            }
        }else if(nodes.get(0) instanceof PrimaryExp){
            return ((PrimaryExp)nodes.get(0)).generateIntermediateCode();
        }else{
            super.generateIntermediateCode();
            Value temp=Allocator.generateVariableValue();
            IntermediateCode.add(new CallFunction(((Lexicality)nodes.get(0)).getContent(),temp));
            return temp;
        }
        return null;
    }
}
