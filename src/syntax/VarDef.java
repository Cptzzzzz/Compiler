package syntax;

import lexical.LexicalitySupporter;

public class VarDef extends ParserUnit {
    VarDef() {
        name = "VarDef";
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
                //todo 错误处理
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
}
