package frontend.syntax;

import frontend.lexical.LexicalitySupporter;
import util.CompilerMode;

public class FuncDef extends ParserUnit {
    FuncDef() {
        type = "FuncDef";
    }

    public static FuncDef parser(LexicalitySupporter lexicalitySupporter) {
        if(CompilerMode.getInstance().isDebug())
            System.out.println("FuncDef");
        FuncDef funcDef = new FuncDef();
        funcDef.add(FuncType.parser(lexicalitySupporter));
        funcDef.add(lexicalitySupporter.readAndNext());
        if (lexicalitySupporter.read().getType().equals("LPARENT")) {
            funcDef.add(lexicalitySupporter.readAndNext());
            if (FuncFParams.pretreat(lexicalitySupporter)) {
                funcDef.add(FuncFParams.parser(lexicalitySupporter));
            }
            if (lexicalitySupporter.read().getType().equals("RPARENT")) {
                funcDef.add(lexicalitySupporter.readAndNext());
            } else {
                //todo 错误处理
            }
        }
        funcDef.add(Block.parser(lexicalitySupporter));
        return funcDef;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        LexicalitySupporter lexicalitySupporter1 = new LexicalitySupporter(lexicalitySupporter.getPointer());
        if (FuncType.pretreat(lexicalitySupporter1)) {
            lexicalitySupporter1.next();
        } else {
            return false;
        }
        return lexicalitySupporter1.read().getType().equals("IDENFR");
    }
}
