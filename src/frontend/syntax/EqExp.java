package frontend.syntax;

import frontend.lexical.Lexicality;
import frontend.lexical.LexicalitySupporter;
import util.Node;

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
        while (eqExp.nodes.get(0) instanceof EqExp) {
            nodes.add(0, eqExp.nodes.get(2));
            nodes.add(0, eqExp.nodes.get(1));
            eqExp = ((EqExp) eqExp.nodes.get(0));
        }
        nodes.add(0, eqExp.nodes.get(0));
        this.nodes = nodes;
        super.eliminateLeftRecursion();
    }

    public void buildLeftRecursion() {
        int length = nodes.size() / 2;
        if (!(length == 0 || nodes.get(0) instanceof EqExp)) {
            EqExp eqExp = new EqExp(), temp;
            eqExp.add(nodes.get(0));
            for (int i = 0; i < length; i++) {
                temp = new EqExp();
                temp.add(eqExp);
                temp.add(nodes.get(1 + 2 * i));
                temp.add(nodes.get(2 * (1 + i)));
                eqExp = temp;
            }
            nodes = eqExp.nodes;
        }
        super.buildLeftRecursion();
    }
}
