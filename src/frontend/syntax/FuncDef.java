package frontend.syntax;

import frontend.lexical.Lexicality;
import frontend.util.LexicalitySupporter;
import frontend.util.*;
import midend.ir.FuncEnd;
import midend.ir.FuncEntry;
import midend.util.IRSupporter;
import midend.util.Value;
import util.ErrorWriter;

import java.util.ArrayList;

public class FuncDef extends ParserUnit {
    FuncDef() {
        setType("FuncDef");
    }

    public static FuncDef parser(LexicalitySupporter lexicalitySupporter) {
        FuncDef funcDef = new FuncDef();
        funcDef.add(FuncType.parser(lexicalitySupporter));
        funcDef.add(lexicalitySupporter.readAndNext());
        if (lexicalitySupporter.read().getType().equals("LPARENT")) {
            funcDef.add(lexicalitySupporter.readAndNext());
            if (FuncFParams.pretreat(lexicalitySupporter))
                funcDef.add(FuncFParams.parser(lexicalitySupporter));
            if (lexicalitySupporter.read().getType().equals("RPARENT")) {
                funcDef.add(lexicalitySupporter.readAndNext());
            } else {
                funcDef.add(new Lexicality(")", "RPARENT"));
                ErrorWriter.add(lexicalitySupporter.getLastLineNumber(), 'j');
            }
        }
        funcDef.add(Block.parser(lexicalitySupporter));
        return funcDef;
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        LexicalitySupporter lexicalitySupporter1 = new LexicalitySupporter(lexicalitySupporter.getPointer());
        if (FuncType.pretreat(lexicalitySupporter1)) {
            lexicalitySupporter1.next();
        } else {
            return false;
        }
        return lexicalitySupporter1.read().getType().equals("IDENFR");
    }

    public void semantic() {
        String name = getNode(1).getContent();
        boolean value = getNode(0).getNode(0).getContent().equals("int");
        ArrayList<Integer> dimensions = new ArrayList<>();
        if (nodes.size() == 6)
            dimensions = ((FuncFParams) getNode(3)).getDimensions();
        if (FunctionTable.getInstance().isExist(name)) {
            ErrorWriter.add(getNode(1).getLineNumber(), 'b');
        } else {
            FunctionTable.getInstance().add(new Function(name, value, dimensions));
        }
        SymbolTable.getInstance().push(((Block) getNode(nodes.size() - 1)).getNumber());
        ((Block) getNode(nodes.size() - 1)).checkReturn();
        super.semantic();
    }

    public void setState(State state) {
        this.state = new State(state.getLoopNumber(), state.getIfNumber(), state.isHaveElse(), getNode(0).getNode(0).getContent().equals("int"), state.getBlockNumber(), state.getLAndNumber(), state.getLOrNumber());
        for (Node node : nodes)
            if (node instanceof ParserUnit)
                ((ParserUnit) node).setState(this.state);
    }

    @Override
    public Value generateIR() {
        IRSupporter.getInstance().addIRCode(new FuncEntry(getNode(1).getContent() + "_function"));
        if (nodes.size() == 6) {
            ((FuncFParams) getNode(3)).generateIR();
        }
        ((Block) getNode(nodes.size() - 1)).generateIR();
        IRSupporter.getInstance().addIRCode(new FuncEnd(getNode(1).getContent() + "_function"));
        return null;
    }
}
