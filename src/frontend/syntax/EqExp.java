package frontend.syntax;

import frontend.util.LexicalitySupporter;
import frontend.util.Node;
import frontend.util.ParserUnit;

import java.util.ArrayList;

public class EqExp extends ParserUnit {
    EqExp() {
        setType("EqExp");
    }

    public static EqExp parser(LexicalitySupporter lexicalitySupporter) {
        EqExp eqExp = new EqExp();
        eqExp.add(RelExp.parser(lexicalitySupporter));
        while (lexicalitySupporter.read().getType().equals("EQL") ||
                lexicalitySupporter.read().getType().equals("NEQ")) {
            eqExp.add(lexicalitySupporter.readAndNext());
            eqExp.add(RelExp.parser(lexicalitySupporter));
        }
        eqExp.buildLeftRecursion();
        return eqExp;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return RelExp.pretreat(lexicalitySupporter);
    }

    public void eliminateLeftRecursion() {
        ArrayList<Node> nodes = new ArrayList<>();
        EqExp eqExp = this;
        while (eqExp.getNode(0) instanceof EqExp) {
            nodes.add(0, eqExp.getNode(2));
            nodes.add(0, eqExp.getNode(1));
            eqExp = ((EqExp) eqExp.getNode(0));
        }
        nodes.add(0, eqExp.getNode(0));
        this.nodes = nodes;
        super.eliminateLeftRecursion();
    }

    public void buildLeftRecursion() {
        int length = nodes.size() / 2;
        if (!(length == 0 || getNode(0) instanceof EqExp)) {
            EqExp eqExp = new EqExp(), temp;
            eqExp.add(getNode(0));
            for (int i = 0; i < length; i++) {
                temp = new EqExp();
                temp.add(eqExp);
                temp.add(getNode(1 + 2 * i));
                temp.add(getNode(2 * (1 + i)));
                eqExp = temp;
            }
            nodes = eqExp.nodes;
        }
        super.buildLeftRecursion();
    }
}
