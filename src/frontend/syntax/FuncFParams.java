package frontend.syntax;

import frontend.lexical.LexicalitySupporter;

public class FuncFParams extends ParserUnit {
    FuncFParams() {
        type = "FuncFParams";
    }

    public static FuncFParams parser(LexicalitySupporter lexicalitySupporter) {
        FuncFParams funcFParams = new FuncFParams();
        funcFParams.add(FuncFParam.parser(lexicalitySupporter));
        while (lexicalitySupporter.read().getType().equals("COMMA")) {
            funcFParams.add(lexicalitySupporter.readAndNext());
            funcFParams.add(FuncFParam.parser(lexicalitySupporter));
        }
        return funcFParams;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return FuncFParam.pretreat(lexicalitySupporter);
    }
}
