package frontend.syntax;

import frontend.lexical.LexicalitySupporter;
import util.CompilerMode;
import util.Node;

public class MainFuncDef extends ParserUnit {
    MainFuncDef() {
        setType("MainFuncDef");
    }


    public static MainFuncDef parser(LexicalitySupporter lexicalitySupporter) {
        if (CompilerMode.getInstance().isDebug())
            System.out.println("MainFuncDef");
        MainFuncDef mainFuncDef = new MainFuncDef();
        for (int i = 0; i < 4; i++) {
            mainFuncDef.add(lexicalitySupporter.readAndNext());
        }
        mainFuncDef.add(Block.parser(lexicalitySupporter));
        return mainFuncDef;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        LexicalitySupporter lexicalitySupporter1 = new LexicalitySupporter(lexicalitySupporter.getPointer());
        if (lexicalitySupporter1.read().getType().equals("INTTK")) {
            lexicalitySupporter1.next();
        } else {
            return false;
        }
        if (lexicalitySupporter1.read().getType().equals("MAINTK")) {
            lexicalitySupporter1.next();
        } else {
            return false;
        }
        if (lexicalitySupporter1.read().getType().equals("LPARENT")) {
            lexicalitySupporter1.next();
        } else {
            return false;
        }
        if (lexicalitySupporter1.read().getType().equals("RPARENT")) {
            lexicalitySupporter1.next();
        } else {
            return false;
        }
        return true;
    }

    public void setState(State state) {
        this.state = new State(state.getLoopNumber(), state.getIfNumber(), state.isHaveElse(), true);
        for (Node node : nodes) {
            if (node instanceof ParserUnit)
                ((ParserUnit) node).setState(this.state);
        }
    }

    @Override
    public void semantic() {
        ((Block) nodes.get(nodes.size() - 1)).checkReturn();
        super.semantic();
    }
}
