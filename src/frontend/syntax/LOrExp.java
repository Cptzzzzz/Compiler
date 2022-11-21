package frontend.syntax;

import frontend.lexical.Lexicality;
import frontend.lexical.LexicalitySupporter;
import util.Node;

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
        while (lOrExp.nodes.get(0) instanceof LOrExp) {
            nodes.add(0, lOrExp.nodes.get(2));
            nodes.add(0, lOrExp.nodes.get(1));
            lOrExp = ((LOrExp) lOrExp.nodes.get(0));
        }
        nodes.add(0, lOrExp.nodes.get(0));
        this.nodes = nodes;
        super.eliminateLeftRecursion();
    }

    public void buildLeftRecursion() {
        int length = nodes.size() / 2;
        if (!(length == 0 || nodes.get(0) instanceof LOrExp)) {
            LOrExp lOrExp = new LOrExp(), temp;
            lOrExp.add(nodes.get(0));
            for (int i = 0; i < length; i++) {
                temp = new LOrExp();
                temp.add(lOrExp);
                temp.add(nodes.get(1 + 2 * i));
                temp.add(nodes.get(2 * (1 + i)));
                lOrExp = temp;
            }
            nodes = lOrExp.nodes;
        }
        super.buildLeftRecursion();
    }
}
