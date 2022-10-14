package syntax;

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
