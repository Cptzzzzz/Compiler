package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.Error;
import util.ErrorWriter;
import util.Node;

import java.util.ArrayList;

public class UnaryExp extends ParserUnit {
    UnaryExp() {
        type = "UnaryExp";
    }

    public static UnaryExp parser(LexicalitySupporter lexicalitySupporter) {
        UnaryExp unaryExp = new UnaryExp();
        if (PrimaryExp.pretreat(lexicalitySupporter)) {
            unaryExp.add(PrimaryExp.parser(lexicalitySupporter));
        } else if (lexicalitySupporter.read().getType().equals("IDENFR")) {
            unaryExp.add(lexicalitySupporter.readAndNext());
            if (lexicalitySupporter.read().getType().equals("LPARENT")) {
                unaryExp.add(lexicalitySupporter.readAndNext());
                if (FuncRParams.pretreat(lexicalitySupporter)) {
                    unaryExp.add(FuncRParams.parser(lexicalitySupporter));
                }
                if (lexicalitySupporter.read().getType().equals("RPARENT")) {
                    unaryExp.add(lexicalitySupporter.readAndNext());
                } else {
                    unaryExp.add(new Lexicality(")", "RPARENT"));
                    ErrorWriter.add(new Error(lexicalitySupporter.getLastLineNumber(), 'j'));
                }
            }
        } else if (UnaryOp.pretreat(lexicalitySupporter)) {
            unaryExp.add(UnaryOp.parser(lexicalitySupporter));
            unaryExp.add(UnaryExp.parser(lexicalitySupporter));
        }
        return unaryExp;
    }


    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (PrimaryExp.pretreat(lexicalitySupporter)) {
            return true;
        } else if (lexicalitySupporter.read().getType().equals("IDENFR")) {
            return true;
        } else if (UnaryOp.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }

    public void setup() {
        if (nodes.get(0) instanceof Lexicality) {
            Function function = getFunction();
            functionTable.judge(function, ((Lexicality) nodes.get(0)).getLineNumber());
            if (functionTable.isVoid(function.getName())) {
                ((ParserUnit) parent).judgeVoid();
            }
        }
        super.setup();
    }

    public Function getFunction() {
        Function function = new Function();
        function.setName(((Lexicality) nodes.get(0)).getContent());
        function.setParams(new ArrayList<>());
        for (Node node : nodes) {
            if (node instanceof FuncRParams)
                function.setParams(((FuncRParams) node).getParams());
        }
        return function;
    }
}
