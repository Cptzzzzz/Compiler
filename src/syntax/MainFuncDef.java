package syntax;

import intermediate.FuncDeclaration;
import intermediate.IntermediateCode;
import intermediate.Value;
import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.CompilerMode;
import util.Error;
import util.ErrorWriter;

public class MainFuncDef extends ParserUnit {
    MainFuncDef() {
        type = "MainFuncDef";
    }

    public static MainFuncDef parser(LexicalitySupporter lexicalitySupporter) {
        if (CompilerMode.getDebug())
            System.out.println("MainFuncDef");
        MainFuncDef mainFuncDef = new MainFuncDef();
        for (int i = 0; i < 4; i++) {
            mainFuncDef.add(lexicalitySupporter.readAndNext());
        }
        mainFuncDef.add(Block.parser(lexicalitySupporter));
        return mainFuncDef;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        LexicalitySupporter lexicalitySupporter1 = new LexicalitySupporter(lexicalitySupporter.getPointer());
        if (lexicalitySupporter1.read().getType().equals("INTTK")) {
            lexicalitySupporter1.next();
        } else {
            return false;
        }
        if (lexicalitySupporter1.read().getType().equals("MAINTK")) {
            lexicalitySupporter1.next();
        } else {
            return false;
        }
        if (lexicalitySupporter1.read().getType().equals("LPARENT")) {
            lexicalitySupporter1.next();
        } else {
            return false;
        }
        if (lexicalitySupporter1.read().getType().equals("RPARENT")) {
            lexicalitySupporter1.next();
        } else {
            return false;
        }
        return true;
    }

    public void setup() {
        super.setup();
        if (!isReturned()) {
            Block block = (Block) nodes.get(nodes.size() - 1);
            ErrorWriter.add(new Error(((Lexicality) block.nodes.get(block.nodes.size() - 1)).getLineNumber()
                    , 'g'));
        }
    }

    public boolean isReturned(){
        return ((Block) nodes.get(nodes.size()-1)).isReturned();
    }

    public Value generateIntermediateCode() {
        IntermediateCode.add(new FuncDeclaration("main",true,0,getAllVariableTable()));
        return super.generateIntermediateCode();
    }
}
