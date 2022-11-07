package syntax;

import intermediate.Declaration;
import intermediate.IntermediateCode;
import intermediate.Value;
import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.Error;
import util.ErrorWriter;
import util.Node;

import java.util.ArrayList;

public class ConstDef extends ParserUnit {
    ConstDef() {
        type = "ConstDef";
    }

    public static ConstDef parser(LexicalitySupporter lexicalitySupporter) {
        ConstDef constDef = new ConstDef();
        constDef.add(lexicalitySupporter.readAndNext());
        while (lexicalitySupporter.read().getType().equals("LBRACK")) {
            constDef.add(lexicalitySupporter.readAndNext());
            constDef.add(ConstExp.parser(lexicalitySupporter));
            if (lexicalitySupporter.read().getType().equals("RBRACK")) {
                constDef.add(lexicalitySupporter.readAndNext());
            } else {
                constDef.add(new Lexicality("]", "RBRACK"));
                ErrorWriter.add(new Error(lexicalitySupporter.getLastLineNumber(), 'k'));
            }
        }
        if (lexicalitySupporter.read().getType().equals("ASSIGN")) {
            constDef.add(lexicalitySupporter.readAndNext());
            constDef.add(ConstInitVal.parser(lexicalitySupporter));
        }
        return constDef;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (lexicalitySupporter.read().getType().equals("IDENFR")) {
            return true;
        }
        return false;
    }

    public void setup(){
        Variable variable=new Variable();
        variable.setName(((Lexicality) nodes.get(0)).getContent());
        variable.setConst(true);
        ArrayList<Integer> dimensions=new ArrayList<>();
        for(Node node:nodes){
            if(node instanceof ConstExp)
                dimensions.add(((ConstExp)node).getValue());
        }
        variable.setDimensions(dimensions);
        variable.setValues(((ConstInitVal)nodes.get(nodes.size()-1)).getValues());
        variableTable.add(variable,((Lexicality) nodes.get(0)).getLineNumber());
        super.setup();
    }

    public Value generateIntermediateCode(){
        IntermediateCode.add(new Declaration(variableTable.getVariableInstance(((Lexicality) nodes.get(0)).getContent())));
        return null;
    }
}
