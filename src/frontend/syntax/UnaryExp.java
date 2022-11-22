package frontend.syntax;

import frontend.lexical.Lexicality;
import frontend.util.LexicalitySupporter;
import frontend.util.Function;
import frontend.util.FunctionTable;
import frontend.util.ParserUnit;
import util.ErrorWriter;

import java.util.ArrayList;
import java.util.Objects;

public class UnaryExp extends ParserUnit {
    UnaryExp() {
        setType("UnaryExp");
    }

    public static UnaryExp parser(LexicalitySupporter lexicalitySupporter) {
        UnaryExp unaryExp = new UnaryExp();
        if (PrimaryExp.pretreat(lexicalitySupporter))
            unaryExp.add(PrimaryExp.parser(lexicalitySupporter));
        else if (lexicalitySupporter.read().getType().equals("IDENFR")) {
            unaryExp.add(lexicalitySupporter.readAndNext());
            if (lexicalitySupporter.read().getType().equals("LPARENT")) {
                unaryExp.add(lexicalitySupporter.readAndNext());
                if (FuncRParams.pretreat(lexicalitySupporter))
                    unaryExp.add(FuncRParams.parser(lexicalitySupporter));
                if (lexicalitySupporter.read().getType().equals("RPARENT"))
                    unaryExp.add(lexicalitySupporter.readAndNext());
                else {
                    unaryExp.add(new Lexicality(")", "RPARENT"));
                    ErrorWriter.add(lexicalitySupporter.getLastLineNumber(), 'j');
                }
            }
        } else if (UnaryOp.pretreat(lexicalitySupporter)) {
            unaryExp.add(UnaryOp.parser(lexicalitySupporter));
            unaryExp.add(UnaryExp.parser(lexicalitySupporter));
        }
        return unaryExp;
    }


    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (PrimaryExp.pretreat(lexicalitySupporter))
            return true;
        else if (lexicalitySupporter.read().getType().equals("IDENFR"))
            return true;
        else return UnaryOp.pretreat(lexicalitySupporter);
    }

    public int getInteger() {
        if (getNode(0) instanceof UnaryOp)
            return (getNode(0).nodes.get(0).getType().equals("PLUS") ? 1 : -1) * ((UnaryExp) nodes.get(1)).getInteger();
        else
            return ((PrimaryExp) nodes.get(0)).getInteger();
    }

    @Override
    public void semantic() {
        if (getNode(0).getType().equals("IDENFR")) {
            if (!FunctionTable.getInstance().isExist(getNode(0).getContent()))
                ErrorWriter.add(getNode(0).getLineNumber(), 'c');
            else {
                Function function = FunctionTable.getInstance().get(getNode(0).getContent());
                ArrayList<Integer> dimensions = function.getDimensions();
                ArrayList<Integer> params = new ArrayList<>();
                if (nodes.size() == 4)
                    params = ((FuncRParams) getNode(2)).getParamsDimensions();
                if (params.size() != dimensions.size())
                    ErrorWriter.add(getNode(0).getLineNumber(), 'd');
                else {
                    int length = params.size();
                    for (int i = 0; i < length; i++) {
                        if (!Objects.equals(params.get(i), dimensions.get(i))) {
                            ErrorWriter.add(getNode(0).getLineNumber(), 'e');
                            break;
                        }
                    }
                }

            }
        }
        super.semantic();
    }

    public int getDimension() {
        if (getNode(0).getType().equals("PrimaryExp")) {
            return ((PrimaryExp) getNode(0)).getDimension();
        } else if (getNode(0).getType().equals("UnaryOp")) {
            return ((UnaryExp) getNode(1)).getDimension();
        } else {
            if (!FunctionTable.getInstance().isExist(getNode(0).getContent())) {
                ErrorWriter.add(getNode(0).getLineNumber(), 'c');
                return 0;
            } else {
                Function function = FunctionTable.getInstance().get(getNode(0).getContent());
                return function.isValue() ? 0 : 3;
            }
        }
    }
}
