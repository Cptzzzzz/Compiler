package syntax;

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
        int length=nodes.size();
        for(int i=0;i<length;i++){
            if(nodes.get(i) instanceof ConstExp)//todo 补全数组长度的逻辑
                dimensions.add(0);
        }
        variable.setDimensions(dimensions);
        variableTable.add(variable,((Lexicality) nodes.get(0)).getLineNumber());
        super.setup();
    }
}
