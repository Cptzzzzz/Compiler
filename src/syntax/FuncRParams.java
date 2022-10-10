package syntax;

import lexical.LexicalitySupporter;

public class FuncRParams extends ParserUnit {
    FuncRParams() {
        name = "FuncRParams";
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
        if (Exp.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }
}
