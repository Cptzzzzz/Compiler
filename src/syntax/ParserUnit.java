package syntax;

import lexical.Lexicality;
import lexical.LexicalitySupporter;
import util.Error;
import util.ErrorWriter;
import util.Node;

import java.util.ArrayList;

public class ParserUnit extends Node {
    public ParserUnit() {
    }

    public static CompUnit treeBuilder() {
        LexicalitySupporter lexicalitySupporter = new LexicalitySupporter();
        return CompUnit.parser(lexicalitySupporter);
    }

    public static ParserUnit parser(LexicalitySupporter lexicalitySupporter) {
        return new ParserUnit();
    }

    public static boolean pretreat(LexicalitySupporter lexicalitySupporter) {
        return true;
    }

    public String toString() {
        return String.format("<%s>", getType());
    }

    public void add(Node node) {
        if (nodes == null) {
            nodes = new ArrayList<Node>();
        }
        nodes.add(node);
    }

    VariableTable variableTable;
    FunctionTable functionTable;

    public void buildFunctionTable(FunctionTable functionTable) {
        this.functionTable = functionTable;
        for (Node node : nodes) {
            if (node instanceof ParserUnit)
                ((ParserUnit) node).buildFunctionTable(functionTable);
        }
    }

    public void buildVariableTable(VariableTable variableTable) {
        this.variableTable = variableTable;
        for (Node node : nodes) {
            if (node instanceof ParserUnit)
                ((ParserUnit) node).buildVariableTable(variableTable);
        }
    }

    public void setup() {
        for (Node node : nodes) {
            if (node instanceof ParserUnit)
                ((ParserUnit) node).setup();
        }
    }

    public void getVariable(String name, int line, boolean isConst) {
        if (getType().equals("Block")) {
            if (variableTable.isExist(name)) {
                if (variableTable.isConst(name) && isConst)
                    ErrorWriter.add(new Error(line, 'h'));
                return;
            }
        } else if (parent == null) {
            if (!variableTable.isExist(name)) {
                if (!isConst)
                    ErrorWriter.add(new Error(line, 'c'));
            } else if (variableTable.isConst(name) && isConst) {
                ErrorWriter.add(new Error(line, 'h'));
            }
            return;
        }
        ((ParserUnit) parent).getVariable(name, line, isConst);
    }

    public int getVariableDimension(String name) {
        if (getType().equals("Block")) {
            if (variableTable.isExist(name)) {
                return variableTable.getDimension(name);
            }
        } else if (parent == null) {
            if (variableTable.isExist(name)) {
                return variableTable.getDimension(name);
            }
        }
        return ((ParserUnit) parent).getVariableDimension(name);
    }

    public boolean shouldReturnValue() {
        if (getType().equals("FuncDef")) {
            return nodes.get(0).nodes.get(0).getType().equals("INTTK");
        } else if (getType().equals("MainFuncDef")) return true;
        return ((ParserUnit) parent).shouldReturnValue();
    }

    public boolean isLoop() {
        if (getType().equals("Stmt")) {
            if (((Stmt) this).judgeLoop()) return true;
        } else if (parent == null) {
            return false;
        }
        return ((ParserUnit) parent).isLoop();
    }

    public int getVariableValue(String name, ArrayList<Integer> args) {
        if (getType().equals("Block") || parent == null) {
            if (variableTable.isConst(name)) {
                return variableTable.getValue(name, args);
            }
        }
        return ((ParserUnit) parent).getVariableValue(name, args);
    }

    public String generateIntermediateCode() {
        for (Node node : nodes) {
            if (node instanceof ParserUnit)
                ((ParserUnit) node).generateIntermediateCode();
        }
        return null;
    }

    public Variable getVariableInstance(String name){
        if(getType().equals("Block")||parent==null){
            if(variableTable.isExist(name))
                return variableTable.getVariableInstance(name);
        }
        return ((ParserUnit)parent).getVariableInstance(name);
    }
}
