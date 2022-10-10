package syntax;

import lexical.LexicalitySupporter;

public class FuncFParam extends ParserUnit {
    FuncFParam() {
        name = "FuncFParam";
    }

    public static FuncFParam parser(LexicalitySupporter lexicalitySupporter) {
        FuncFParam funcFParam = new FuncFParam();
        funcFParam.add(BType.parser(lexicalitySupporter));
        funcFParam.add(lexicalitySupporter.readAndNext());
        if (lexicalitySupporter.read().getType().equals("LBRACK")) {
            funcFParam.add(lexicalitySupporter.readAndNext());
            if (lexicalitySupporter.read().getType().equals("RBRACK")) {
                funcFParam.add(lexicalitySupporter.readAndNext());
                while (lexicalitySupporter.read().getType().equals("LBRACK")) {
                    funcFParam.add(lexicalitySupporter.readAndNext());
                    funcFParam.add(ConstExp.parser(lexicalitySupporter));
                    if (lexicalitySupporter.read().getType().equals("RBRACK")) {
                        funcFParam.add(lexicalitySupporter.readAndNext());
                    } else {
                        //todo 错误处理
                    }
                }
            } else {
                //todo 错误处理
            }
        }
        return funcFParam;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (BType.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }
}
