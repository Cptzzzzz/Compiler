package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.Error;
import util.ErrorWriter;
import util.Node;

import java.util.ArrayList;
import java.util.Collection;

public class LVal extends ParserUnit {
    LVal() {
        type = "LVal";
    }

    public static LVal parser(LexicalitySupporter lexicalitySupporter) {
        LVal lVal = new LVal();
        lVal.add(lexicalitySupporter.readAndNext());
        while (lexicalitySupporter.read().getType().equals("LBRACK")) {
            lVal.add(lexicalitySupporter.readAndNext());
            lVal.add(Exp.parser(lexicalitySupporter));
            if (lexicalitySupporter.read().getType().equals("RBRACK")) {
                lVal.add(lexicalitySupporter.readAndNext());
            } else {
                lVal.add(new Lexicality("]", "RBRACK"));
                ErrorWriter.add(new Error(lexicalitySupporter.getLastLineNumber(), 'k'));
            }
        }
        return lVal;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        LexicalitySupporter lexicalitySupporter1 = new LexicalitySupporter(lexicalitySupporter.getPointer());
        if (lexicalitySupporter1.read().getType().equals("IDENFR")) {
            lexicalitySupporter1.next();
        } else {
            return false;
        }
        if (lexicalitySupporter1.read().getType().equals("LPARENT")) {
            return false;
        } else {
            return true;
        }
    }

    public void setup() {
        getVariable(getVariableName(), getVariableLineNumber(), false);
        super.setup();
    }

    public String getVariableName() {
        return ((Lexicality) nodes.get(0)).getContent();
    }

    public int getVariableLineNumber() {
        return ((Lexicality) nodes.get(0)).getLineNumber();
    }

    public ArrayList<LVal> getLVal() {
        ArrayList<LVal> res=new ArrayList<>();
        res.add(this);
        return res;
    }

    public int getDimension() {
        return    getVariableDimension(getVariableName())-  (nodes.size() - 1) / 3;
    }
}
