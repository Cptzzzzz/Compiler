package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.OutputWriter;

import java.util.ArrayList;

public class UnaryExp extends ParserUnit {
    UnaryExp() {
        name = "UnaryExp";
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
                unaryExp.add(lexicalitySupporter.readAndNext());
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
}
