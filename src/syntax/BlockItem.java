package syntax;

import lexical.LexicalitySupporter;

public class BlockItem extends ParserUnit {
    BlockItem() {
        type = "BlockItem";
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

    public boolean isReturnStmt(){
        if(nodes.get(0) instanceof Decl)return false;
        return ((Stmt) nodes.get(0)).isReturnStmt();
    }
    public static BlockItem buildReturn(){
        BlockItem blockItem=new BlockItem();
        blockItem.add(Stmt.buildReturn());
        return blockItem;
    }
}
