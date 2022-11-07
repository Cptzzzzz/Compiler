package syntax;

import intermediate.Assign;
import intermediate.Declaration;
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
        if(variableTable.isGlobal()){
            if(nodes.get(nodes.size()-1) instanceof InitVal)
                variable.setValues(((InitVal)nodes.get(nodes.size()-1)).getIntValues());
            else{
                ArrayList<Integer> res=new ArrayList<>();
                for(int i=variable.getLength()-1;i>=0;i--)
                    res.add(0);
                variable.setValues(res);
            }
        }
        variableTable.add(variable,((Lexicality) nodes.get(0)).getLineNumber());
        super.setup();
    }

    public Value generateIntermediateCode(){
        Declaration declaration=new Declaration(variableTable.getVariableInstance(((Lexicality) nodes.get(0)).getContent()));
        if(nodes.get(nodes.size()-1) instanceof InitVal && !variableTable.isGlobal()){
            ArrayList<Value> values=((InitVal)nodes.get(nodes.size()-1)).getValues();
            declaration.setValues(values);
        }
        IntermediateCode.add(declaration);
        return null;
    }
}
