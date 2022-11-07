package syntax;

import intermediate.FuncDeclaration;
import intermediate.FuncParam;
import intermediate.IntermediateCode;
import intermediate.Value;
import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.CompilerMode;
import util.Error;
import util.ErrorWriter;

import java.util.ArrayList;

public class FuncDef extends ParserUnit {
    FuncDef() {
        type = "FuncDef";
    }

    public static FuncDef parser(LexicalitySupporter lexicalitySupporter) {
        if (CompilerMode.getDebug())
            System.out.println("FuncDef");
        FuncDef funcDef = new FuncDef();
        funcDef.add(FuncType.parser(lexicalitySupporter));
        funcDef.add(lexicalitySupporter.readAndNext());
        if (lexicalitySupporter.read().getType().equals("LPARENT")) {
            funcDef.add(lexicalitySupporter.readAndNext());
            if (FuncFParams.pretreat(lexicalitySupporter)) {
                funcDef.add(FuncFParams.parser(lexicalitySupporter));
            }
            if (lexicalitySupporter.read().getType().equals("RPARENT")) {
                funcDef.add(lexicalitySupporter.readAndNext());
            } else {
                funcDef.add(new Lexicality(")", "RPARENT"));
                ErrorWriter.add(new Error(lexicalitySupporter.getLastLineNumber(), 'j'));
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
        if (lexicalitySupporter1.read().getType().equals("IDENFR")) {
            return true;
        } else {
            return false;
        }
    }

    public void setup() {
        Function function = new Function();
        function.setReturnValue(nodes.get(0).nodes.get(0).getType().equals("INTTK"));
        function.setName(((Lexicality) nodes.get(1)).getContent());
        ArrayList<Variable> params = getParams();
        function.setParams(params);
        functionTable.add(function, ((Lexicality) nodes.get(1)).getLineNumber());

        Block block = (Block) nodes.get(nodes.size() - 1);
        block.variableTable.add(params, ((Lexicality) nodes.get(1)).getLineNumber());
        super.setup();
        if (nodes.get(0).nodes.get(0).getType().equals("INTTK") && !isReturned()) {
            ErrorWriter.add(new Error(((Lexicality) block.nodes.get(block.nodes.size() - 1)).getLineNumber()
                    , 'g'));
        }
        if (!block.isReturned() && nodes.get(0).nodes.get(0).getType().equals("VOIDTK")) {
            block.addReturn();
        }
    }

    public ArrayList<Variable> getParams() {
        if (nodes.size() == 5) {
            return new ArrayList<>();
        }
        return ((FuncFParams) nodes.get(3)).getParams();
    }

    public boolean isReturned() {
        return ((Block) nodes.get(nodes.size() - 1)).isReturned();
    }

    public Value generateIntermediateCode() {
        Function function = functionTable.getFunctionInstance(((Lexicality) nodes.get(1)).getContent());
        IntermediateCode.add(new FuncDeclaration(function.getName(), function.isReturnValue(), function.params.size(), getAllVariableTable()));
        for (Variable variable : function.params) {
            IntermediateCode.add(new FuncParam(variable.getFinalName(), variable.getDimension() != 0));
        }
        ((Block) nodes.get(nodes.size() - 1)).generateIntermediateCode();
        return null;
    }
}
