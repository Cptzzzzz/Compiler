package syntax;

import intermediate.Allocator;
import lexical.LexicalitySupporter;
import util.CompilerMode;
import util.Node;

public class Block extends ParserUnit {
    Block() {
        type = "Block";
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

    public void buildVariableTable(VariableTable variableTable){
        this.variableTable=new VariableTable(Allocator.generateTableNumber());
        for(Node node:nodes){
            if(node instanceof ParserUnit)
                ((ParserUnit) node).buildVariableTable(this.variableTable);
        }
    }

    public boolean isReturned(){
        if(nodes.size()==2) return false;
        return ((BlockItem) nodes.get(nodes.size()-2)).isReturnStmt();
    }
}
