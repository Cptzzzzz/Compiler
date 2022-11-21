package frontend.syntax;

import frontend.lexical.Lexicality;
import frontend.lexical.LexicalitySupporter;
import util.Node;

import java.util.ArrayList;

public class RelExp extends ParserUnit {
    RelExp() {
        setType("RelExp");
    }

    public static RelExp parser(LexicalitySupporter lexicalitySupporter) {
        RelExp relExp = new RelExp();
        relExp.add(AddExp.parser(lexicalitySupporter));
        while (lexicalitySupporter.read().getType().equals("LSS") ||
                lexicalitySupporter.read().getType().equals("LEQ") ||
                lexicalitySupporter.read().getType().equals("GRE") ||
                lexicalitySupporter.read().getType().equals("GEQ")) {
            relExp.add(lexicalitySupporter.readAndNext());
            relExp.add(AddExp.parser(lexicalitySupporter));
        }
        relExp.buildLeftRecursion();
        return relExp;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return AddExp.pretreat(lexicalitySupporter);
    }

    public void eliminateLeftRecursion() {
        ArrayList<Node> nodes = new ArrayList<>();
        RelExp relExp = this;
        while (relExp.nodes.get(0) instanceof RelExp) {
            nodes.add(0, relExp.nodes.get(2));
            nodes.add(0, relExp.nodes.get(1));
            relExp = ((RelExp) relExp.nodes.get(0));
        }
        nodes.add(0, relExp.nodes.get(0));
        this.nodes = nodes;
        super.eliminateLeftRecursion();
    }

    public void buildLeftRecursion() {
        int length = nodes.size() / 2;
        if (!(length == 0 || nodes.get(0) instanceof RelExp)) {
            RelExp relExp = new RelExp(), temp;
            relExp.add(nodes.get(0));
            for (int i = 0; i < length; i++) {
                temp = new RelExp();
                temp.add(relExp);
                temp.add(nodes.get(1 + 2 * i));
                temp.add(nodes.get(2 * (1 + i)));
                relExp = temp;
            }
            nodes = relExp.nodes;
        }
        super.buildLeftRecursion();
    }
}
