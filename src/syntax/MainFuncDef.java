package syntax;

import lexical.LexicalitySupporter;
import util.CompilerMode;

public class MainFuncDef extends ParserUnit {
    MainFuncDef() {
        name = "MainFuncDef";
    }


    public static MainFuncDef parser(LexicalitySupporter lexicalitySupporter) {
        if(CompilerMode.getDebug())
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
}
