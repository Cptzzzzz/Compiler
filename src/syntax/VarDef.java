package syntax;

import intermediate.Assign;
import intermediate.IntermediateCode;
import intermediate.Value;
import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.Error;
import util.ErrorWriter;
import util.Node;

import java.util.ArrayList;

public class VarDef extends ParserUnit {
    VarDef() {
        type = "VarDef";
    }

    public static VarDef parser(LexicalitySupporter lexicalitySupporter) {
        VarDef varDef = new VarDef();
        varDef.add(lexicalitySupporter.readAndNext());
        while (lexicalitySupporter.read().getType().equals("LBRACK")) {
            varDef.add(lexicalitySupporter.readAndNext());
            varDef.add(ConstExp.parser(lexicalitySupporter));
            if (lexicalitySupporter.read().getType().equals("RBRACK")) {
                varDef.add(lexicalitySupporter.readAndNext());
            } else {
                varDef.add(new Lexicality("]", "RBRACK"));
                ErrorWriter.add(new Error(lexicalitySupporter.getLastLineNumber(), 'k'));
            }
        }
        if (lexicalitySupporter.read().getType().equals("ASSIGN")) {
            varDef.add(lexicalitySupporter.readAndNext());
            varDef.add(InitVal.parser(lexicalitySupporter));
        }
        return varDef;
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

    public void setup(){
        Variable variable=new Variable();
        variable.setName(((Lexicality) nodes.get(0)).getContent());
        variable.setConst(false);
        ArrayList<Integer> dimensions=new ArrayList<>();
        for(Node node:nodes){
            if(node instanceof ConstExp)
                dimensions.add(((ConstExp)node).getValue());
        }
        variable.setDimensions(dimensions);
        variableTable.add(variable,((Lexicality) nodes.get(0)).getLineNumber());
        super.setup();
    }

    public String generateIntermediateCode(){
        variableTable.generateIntermediateCode(((Lexicality) nodes.get(0)).getContent());
        if(nodes.get(nodes.size()-1) instanceof InitVal){
            Variable variable=variableTable.getVariableInstance(((Lexicality) nodes.get(0)).getContent());
            ArrayList<Value> values=((InitVal)nodes.get(nodes.size()-1)).getValues();
            if(variable.getDimension()==0){
                IntermediateCode.add(new Assign(
                        new Value(variable.getFinalName()),Assign.NONE,values.get(0)
                ));
            }else if(variable.getDimension()==1){
                int length=values.size();
                for(int i=0;i<length;i++){
                    IntermediateCode.add(new Assign(
                            new Value(variable.getFinalName(),new Value(i)),Assign.NONE,values.get(i)
                    ));
                }
            }else{
                ArrayList<Integer> dimension=variable.getDimensions();
                int l1=dimension.get(0),l2=dimension.get(1);
                for(int i=0;i<l1;i++){
                    for(int j=0;j<l2;j++){
                        IntermediateCode.add(new Assign(
                                new Value(variable.getFinalName(),new Value(i*l2+j)),
                                Assign.NONE,values.get(i*l2+j)
                        ));
                    }
                }
            }
        }
        return null;
    }
}
