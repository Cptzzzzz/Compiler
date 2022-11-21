package frontend.syntax;

import frontend.lexical.LexicalitySupporter;

public class FuncRParams extends ParserUnit {
    FuncRParams() {
        type = "FuncRParams";
    }

    public static FuncRParams parser(LexicalitySupporter lexicalitySupporter) {
        FuncRParams funcRParams = new FuncRParams();
        funcRParams.add(Exp.parser(lexicalitySupporter));
        while (lexicalitySupporter.read().getType().equals("COMMA")) {
            funcRParams.add(lexicalitySupporter.readAndNext());
            funcRParams.add(Exp.parser(lexicalitySupporter));
        }
        return funcRParams;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return Exp.pretreat(lexicalitySupporter);
    }
}
