package frontend.syntax;

import frontend.lexical.Lexicality;
import frontend.lexical.LexicalitySupporter;
import frontend.util.Allocator;
import frontend.util.SymbolTable;
import util.CompilerMode;
import util.ErrorWriter;

public class Block extends ParserUnit {
    private int number;

    Block() {
        setType("Block");
        number = Allocator.getInstance().getBlockNumber();
    }

    public int getNumber() {
        return number;
    }

    public static Block parser(LexicalitySupporter lexicalitySupporter) {
        if (CompilerMode.getInstance().isDebug())
            System.out.println("Block");
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

    public void semantic() {
        SymbolTable.getInstance().push(number);
        super.semantic();
        SymbolTable.getInstance().pop();
    }

    public void checkReturn() {
        boolean flag = false;
        try {
            flag = ((Stmt) nodes.get(nodes.size() - 2) /* BlockItem */.nodes.get(0)/*stmt */).getStmtType() == 7;
        } catch (Exception ignored) {

        }
        if (!flag) {
            if (!state.shouldReturnValue()) {
                Stmt stmt = new Stmt();
                stmt.setStmtType(7);
                stmt.add(new Lexicality("return", "RETURNTK"));
                stmt.add(new Lexicality(";", "COMMA"));
                stmt.setState(state);
                BlockItem blockItem = new BlockItem();
                blockItem.add(stmt);
                nodes.add(nodes.size() - 1, blockItem);
            } else {
                ErrorWriter.add(nodes.get(nodes.size() - 1).getLineNumber(), 'g');
            }
        }
    }
}
