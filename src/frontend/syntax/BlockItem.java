package frontend.syntax;

import frontend.util.LexicalitySupporter;
import frontend.util.ParserUnit;

public class BlockItem extends ParserUnit {
    BlockItem() {
        setType("BlockItem");
        setOutput(false);
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
        return Decl.pretreat(lexicalitySupporter) || Stmt.pretreat(lexicalitySupporter);
    }
}
