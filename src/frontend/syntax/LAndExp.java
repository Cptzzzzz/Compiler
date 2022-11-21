package frontend.syntax;

import frontend.lexical.Lexicality;
import frontend.lexical.LexicalitySupporter;
import util.Node;

import java.util.ArrayList;

public class LAndExp extends ParserUnit {
    LAndExp() {
        type = "LAndExp";
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
        while (lAndExp.nodes.get(0) instanceof LAndExp) {
            nodes.add(0, lAndExp.nodes.get(2));
            nodes.add(0, lAndExp.nodes.get(1));
            lAndExp = ((LAndExp) lAndExp.nodes.get(0));
        }
        nodes.add(0, lAndExp.nodes.get(0));
        this.nodes = nodes;
        super.eliminateLeftRecursion();
    }

    public void buildLeftRecursion() {
        int length = nodes.size() / 2;
        if (!(length == 0 || nodes.get(0) instanceof LAndExp)) {
            LAndExp lAndExp = new LAndExp(), temp;
            lAndExp.add(nodes.get(0));
            for (int i = 0; i < length; i++) {
                temp = new LAndExp();
                temp.add(lAndExp);
                temp.add(nodes.get(1 + 2 * i));
                temp.add(nodes.get(2 * (1 + i)));
                lAndExp = temp;
            }
            nodes = lAndExp.nodes;
        }
        super.buildLeftRecursion();
    }
}
