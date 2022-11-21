package frontend.syntax;

import frontend.lexical.Lexicality;
import frontend.lexical.LexicalitySupporter;
import frontend.util.Function;
import frontend.util.FunctionTable;
import frontend.util.SymbolTable;
import util.CompilerMode;
import util.ErrorWriter;
import util.Node;

import java.util.ArrayList;

public class FuncDef extends ParserUnit {
    FuncDef() {
        setType("FuncDef");
    }

    public static FuncDef parser(LexicalitySupporter lexicalitySupporter) {
        if (CompilerMode.getInstance().isDebug())
            System.out.println("FuncDef");
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
        String name = nodes.get(1).getContent();
        boolean value = nodes.get(0).nodes.get(0).getContent().equals("int");
        ArrayList<Integer> dimensions = new ArrayList<>();
        if (nodes.size() == 6)
            dimensions = ((FuncFParams) nodes.get(3)).getDimensions();
        if (FunctionTable.getInstance().isExist(name)) {
            ErrorWriter.add(nodes.get(1).getLineNumber(), 'b');
        } else {
            FunctionTable.getInstance().add(new Function(name, value, dimensions));
        }
        SymbolTable.getInstance().push(((Block) nodes.get(nodes.size() - 1)).getNumber());
        ((Block) nodes.get(nodes.size() - 1)).checkReturn();
        super.semantic();
    }

    public void setState(State state) {
        this.state = new State(state.getLoopNumber(), state.getIfNumber(), state.isHaveElse(), nodes.get(0).nodes.get(0).getContent().equals("int"));
        for (Node node : nodes) {
            if (node instanceof ParserUnit)
                ((ParserUnit) node).setState(this.state);
        }
    }
}
