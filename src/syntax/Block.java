package syntax;

import lexical.LexicalitySupporter;
import util.CompilerMode;

public class Block extends ParserUnit {
    Block() {
        name = "Block";
    }

    public static Block parser(LexicalitySupporter lexicalitySupporter) {
        if(CompilerMode.getDebug())
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
        if (lexicalitySupporter.read().getType().equals("LBRACE")) {
            return true;
        }
        return false;
    }
}
