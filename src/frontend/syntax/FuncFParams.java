package frontend.syntax;

import frontend.lexical.LexicalitySupporter;
import util.Node;

import java.util.ArrayList;

public class FuncFParams extends ParserUnit {
    FuncFParams() {
        setType("FuncFParams");
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

    public ArrayList<Integer> getDimensions() {
        ArrayList<Integer> res = new ArrayList<>();
        for (Node node : nodes) {
            if (node instanceof FuncFParam)
                res.add(((FuncFParam) node).getDimension());
        }
        return res;
    }
}
