package syntax;

import lexical.LexicalitySupporter;

public class BlockItem extends ParserUnit {
    BlockItem() {
        name = "BlockItem";
        isOutput=false;
    }

    public static BlockItem parser(LexicalitySupporter lexicalitySupporter) {
        BlockItem blockItem = new BlockItem();
        if (Decl.pretreat(lexicalitySupporter)) {
            blockItem.add(Decl.parser(lexicalitySupporter));
        } else if (Stmt.pretreat(lexicalitySupporter)) {
            blockItem.add(Stmt.parser(lexicalitySupporter));
        }
        return blockItem;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        if (Decl.pretreat(lexicalitySupporter) || Stmt.pretreat(lexicalitySupporter)) {
            return true;
        }
        return false;
    }
}
