package frontend.syntax;

import frontend.lexical.LexicalitySupporter;
import util.CompilerMode;

public class Block extends ParserUnit {
    Block() {
        type = "Block";
    }

    public static Block parser(LexicalitySupporter lexicalitySupporter) {
        if(CompilerMode.getInstance().isDebug())
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
}
