package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.Error;
import util.ErrorWriter;
import util.Node;

import java.util.ArrayList;

public class FuncFParam extends ParserUnit {
    FuncFParam() {
        type = "FuncFParam";
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
                        funcFParam.add(new Lexicality("]", "RBRACK"));
                        ErrorWriter.add(new Error(lexicalitySupporter.getLastLineNumber(), 'k'));
                    }
                }
            } else {
                funcFParam.add(new Lexicality("]", "RBRACK"));
                ErrorWriter.add(new Error(lexicalitySupporter.getLastLineNumber(), 'k'));
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

    public Variable getParam() {
        Variable variable = new Variable();
        variable.setConst(false);
        variable.setName(((Lexicality) nodes.get(1)).getContent());
        ArrayList<Integer> dimensions = new ArrayList<>();
        if (nodes.size() != 2) dimensions.add(0);
        for (Node node : nodes) {
            if (node instanceof ConstExp)
                dimensions.add(((ConstExp) node).getValue());
        }
        variable.setDimensions(dimensions);
        return variable;
    }
}
