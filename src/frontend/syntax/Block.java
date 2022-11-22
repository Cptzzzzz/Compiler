package frontend.syntax;

import frontend.lexical.Lexicality;
import frontend.util.LexicalitySupporter;
import frontend.util.*;
import util.ErrorWriter;

public class Block extends ParserUnit {
    Block() {
        setType("Block");
    }

    public int getNumber() {
        return state.getBlockNumber();
    }

    public static Block parser(LexicalitySupporter lexicalitySupporter) {
        Block block = new Block();
        block.add(lexicalitySupporter.readAndNext());
        while (BlockItem.pretreat(lexicalitySupporter)) {
            block.add(BlockItem.parser(lexicalitySupporter));
        }
        block.add(lexicalitySupporter.readAndNext());
        return block;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return lexicalitySupporter.read().getType().equals("LBRACE");
    }

    public void setState(State state) {
        this.state = new State(state.getLoopNumber(), state.getIfNumber(), state.isHaveElse(), state.shouldReturnValue(), Allocator.getInstance().getBlockNumber());
        for (Node node : nodes)
            if (node instanceof ParserUnit)
                ((ParserUnit) node).setState(this.state);
    }

    public void semantic() {
        SymbolTable.getInstance().push(getNumber());
        super.semantic();
        SymbolTable.getInstance().pop();
    }

    public void checkReturn() {
        boolean flag = false;
        try {
            flag = ((Stmt) getNode(nodes.size() - 2) /* BlockItem */.getNode(0)/*stmt */).getStmtType() == 7;
        } catch (Exception ignored) {

        }
        if (!flag) {
            if (!state.shouldReturnValue()) {
                Stmt stmt = new Stmt();
                stmt.setStmtType(7);
                stmt.add(new Lexicality("return", "RETURNTK"));
                stmt.add(new Lexicality(";", "SEMICN"));
                BlockItem blockItem = new BlockItem();
                blockItem.add(stmt);
                blockItem.setState(state);
                nodes.add(nodes.size() - 1, blockItem);
            } else {
                ErrorWriter.add(getNode(nodes.size() - 1).getLineNumber(), 'g');
            }
        }
    }
}
