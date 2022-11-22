package frontend.syntax;

import frontend.util.LexicalitySupporter;
import frontend.util.Node;
import frontend.util.ParserUnit;

import java.util.ArrayList;

public class LAndExp extends ParserUnit {
    LAndExp() {
        setType("LAndExp");
    }

    public static LAndExp parser(LexicalitySupporter lexicalitySupporter) {
        LAndExp lAndExp = new LAndExp();
        lAndExp.add(EqExp.parser(lexicalitySupporter));
        while (lexicalitySupporter.read().getType().equals("AND")) {
            lAndExp.add(lexicalitySupporter.readAndNext());
            lAndExp.add(EqExp.parser(lexicalitySupporter));
        }
        lAndExp.buildLeftRecursion();
        return lAndExp;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return EqExp.pretreat(lexicalitySupporter);
    }

    public void eliminateLeftRecursion() {
        ArrayList<Node> nodes = new ArrayList<>();
        LAndExp lAndExp = this;
        while (lAndExp.getNode(0) instanceof LAndExp) {
            nodes.add(0, lAndExp.getNode(2));
            nodes.add(0, lAndExp.getNode(1));
            lAndExp = ((LAndExp) lAndExp.getNode(0));
        }
        nodes.add(0, lAndExp.getNode(0));
        this.nodes = nodes;
        super.eliminateLeftRecursion();
    }

    public void buildLeftRecursion() {
        int length = nodes.size() / 2;
        if (!(length == 0 || getNode(0) instanceof LAndExp)) {
            LAndExp lAndExp = new LAndExp(), temp;
            lAndExp.add(getNode(0));
            for (int i = 0; i < length; i++) {
                temp = new LAndExp();
                temp.add(lAndExp);
                temp.add(getNode(1 + 2 * i));
                temp.add(getNode(2 * (1 + i)));
                lAndExp = temp;
            }
            nodes = lAndExp.nodes;
        }
        super.buildLeftRecursion();
    }
}
