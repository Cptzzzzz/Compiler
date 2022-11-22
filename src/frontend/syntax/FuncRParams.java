package frontend.syntax;

import frontend.util.LexicalitySupporter;
import frontend.util.Node;
import frontend.util.ParserUnit;

import java.util.ArrayList;

public class FuncRParams extends ParserUnit {
    FuncRParams() {
        setType("FuncRParams");
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

    public ArrayList<Integer> getParamsDimensions() {
        ArrayList<Integer> res = new ArrayList<>();
        for (Node node : nodes) {
            if (node instanceof Exp)
                res.add(((Exp) node).getDimension());
        }
        return res;
    }
}
