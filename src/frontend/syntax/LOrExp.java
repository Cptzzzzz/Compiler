package frontend.syntax;

import frontend.util.LexicalitySupporter;
import frontend.util.Node;
import frontend.util.ParserUnit;

import java.util.ArrayList;

public class LOrExp extends ParserUnit {
    LOrExp() {
        setType("LOrExp");
    }

    public static LOrExp parser(LexicalitySupporter lexicalitySupporter) {
        LOrExp lOrExp = new LOrExp();
        lOrExp.add(LAndExp.parser(lexicalitySupporter));
        while (lexicalitySupporter.read().getType().equals("OR")) {
            lOrExp.add(lexicalitySupporter.readAndNext());
            lOrExp.add(LAndExp.parser(lexicalitySupporter));
        }
        lOrExp.buildLeftRecursion();
        return lOrExp;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return LAndExp.pretreat(lexicalitySupporter);
    }

    public void eliminateLeftRecursion() {
        ArrayList<Node> nodes = new ArrayList<>();
        LOrExp lOrExp = this;
        while (lOrExp.getNode(0) instanceof LOrExp) {
            nodes.add(0, lOrExp.getNode(2));
            nodes.add(0, lOrExp.getNode(1));
            lOrExp = ((LOrExp) lOrExp.getNode(0));
        }
        nodes.add(0, lOrExp.getNode(0));
        this.nodes = nodes;
        super.eliminateLeftRecursion();
    }

    public void buildLeftRecursion() {
        int length = nodes.size() / 2;
        if (!(length == 0 || getNode(0) instanceof LOrExp)) {
            LOrExp lOrExp = new LOrExp(), temp;
            lOrExp.add(getNode(0));
            for (int i = 0; i < length; i++) {
                temp = new LOrExp();
                temp.add(lOrExp);
                temp.add(getNode(1 + 2 * i));
                temp.add(getNode(2 * (1 + i)));
                lOrExp = temp;
            }
            nodes = lOrExp.nodes;
        }
        super.buildLeftRecursion();
    }
}
