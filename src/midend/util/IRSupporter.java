package midend.util;

import midend.ir.*;
import midend.optimization.BasicBlock;
import midend.optimization.FlowGraph;
import util.CompilerMode;
import util.IRWriter;

import java.util.ArrayList;

public class IRSupporter {
    private IRSupporter() {
    }

    private static IRSupporter irSupporter;
    private ArrayList<IRCode> irCodes = new ArrayList<>();

    public static IRSupporter getInstance() {
        if (irSupporter == null)
            reset();
        return irSupporter;
    }

    public static void reset() {
        irSupporter = new IRSupporter();
    }

    public void addIRCode(IRCode irCode) {
        irCodes.add(irCode);
    }

    public void output() {
        if (CompilerMode.getInstance().isIr())
            for (IRCode irCode : irCodes)
                IRWriter.writeln(irCode.toString());
    }

    public ArrayList<IRCode> getIrCodes() {
        return irCodes;
    }

    private FlowGraph flowGraph;

    public void optimize() {
        moveDeclaration();
        while (jumpOptimize()) ;
        buildFlowGraph();
        flowGraph.optimize();
        irCodes = flowGraph.generateIRCodes();
        while (jumpOptimize()) ;
    }

    private void moveDeclaration() {
        ArrayList<IRCode> res = new ArrayList<>();
        ArrayList<IRCode> temp = new ArrayList<>();
        ArrayList<IRCode> declarations = new ArrayList<>();
        boolean flag = false;
        for (IRCode irCode : irCodes) {
            if (!flag) {
                if (irCode instanceof FuncEntry) {
                    flag = true;
                    temp.add(irCode);
                } else {
                    res.add(irCode);
                }
            } else {
                if (irCode instanceof FuncEnd) {
                    temp.add(irCode);
                    res.add(temp.get(0));
                    temp.remove(0);
                    res.addAll(declarations);
                    res.addAll(temp);
                    declarations = new ArrayList<>();
                    temp = new ArrayList<>();
                } else if (irCode instanceof Declaration) {
                    declarations.add(irCode);
                } else {
                    temp.add(irCode);
                }
            }
        }
        irCodes = res;
    }

    public void removeLabel() {
        ArrayList<String> labels = new ArrayList<>();
        for (IRCode irCode : irCodes) {
            if (irCode instanceof Jump) {
                labels.add(((Jump) irCode).getLabel());
            } else if (irCode instanceof Branch) {
                labels.add(((Branch) irCode).getLabel());
            }
        }
        int length = irCodes.size();
        for (int i = length - 1; i >= 0; i--) {
            IRCode irCode = irCodes.get(i);
            if (irCode instanceof Label) {
                if (!labels.contains(((Label) irCode).getLabel()))
                    irCodes.remove(i);
            }
        }
    }

    public boolean jumpOptimize() {
        removeLabel();
        String label = null, next = null;
        int jumpIndex = -1;
        ArrayList<Integer> shouldRemove = new ArrayList<>();
        for (IRCode irCode : irCodes) {
            if (irCode instanceof Jump) {
                if (label != null) {
                    replaceLabel(label, ((Jump) irCode).getLabel());
                }
            } else if (irCode instanceof Label) {
                if (next != null && next.equals(((Label) irCode).getLabel()))
                    shouldRemove.add(jumpIndex);
            }
            if (irCode instanceof Label) {
                label = ((Label) irCode).getLabel();
            } else {
                label = null;
            }
            if (irCode instanceof Jump) {
                next = ((Jump) irCode).getLabel();
                jumpIndex = irCodes.indexOf(irCode);
            } else if (!(irCode instanceof Declaration || irCode instanceof Label)) {
                next = null;
            }
        }
        for (int i = shouldRemove.size() - 1; i >= 0; i--) {
            irCodes.remove((int) shouldRemove.get(i));
        }
        removeLabel();
        return !shouldRemove.isEmpty();
    }

    private void replaceLabel(String oldLabel, String newLabel) {
//        System.out.println("replace " + oldLabel + " with " + newLabel);
        for (IRCode irCode : irCodes) {
            if (irCode instanceof Jump) {
                if (((Jump) irCode).getLabel().equals(oldLabel))
                    ((Jump) irCode).setLabel(newLabel);
            } else if (irCode instanceof Branch) {
                if (((Branch) irCode).getLabel().equals(oldLabel))
                    ((Branch) irCode).setLabel(newLabel);
            }
        }
    }

    public void buildFlowGraph() {
        ArrayList<String> labels = new ArrayList<>();
        boolean flag = true;
        for (IRCode irCode : getIrCodes()) {
            if (flag) {
                irCode.setEntryCode();
                flag = false;
            }
            if (irCode instanceof Jump) {
                labels.add(((Jump) irCode).getLabel());
                flag = true;
            } else if (irCode instanceof Branch) {
                labels.add(((Branch) irCode).getLabel());
                flag = true;
            }
        }
        for (IRCode irCode : getIrCodes()) {
            if (irCode instanceof Label && labels.contains(((Label) irCode).getLabel())) {
                irCode.setEntryCode();
            } else if (irCode instanceof FuncEntry) {
                irCode.setEntryCode();
            }
        }
        int number = -1;
        for (IRCode irCode : getIrCodes()) {
            if (irCode.isEntryCode())
                number++;
            irCode.setBlockNumber(number);
        }
        flowGraph = new FlowGraph();
        BasicBlock basicBlock = null;
        flag = true;
        for (IRCode irCode : getIrCodes()) {
            if (irCode.isEntryCode()) {
//                System.out.println(irCode);
                basicBlock = new BasicBlock();
                basicBlock.setBlockNumber(irCode.getBlockNumber());
                flowGraph.addBasicBlock(basicBlock, flag);
                if (irCode instanceof FuncEntry && ((FuncEntry) irCode).getName().equals("main_function"))
                    flag = false;
            }
            assert basicBlock != null;
            basicBlock.addIRCode(irCode);
        }
        for (BasicBlock basicBlock1 : flowGraph.getBasicBlocks()) {
            IRCode irCode = basicBlock1.getIrCodes().get(basicBlock1.getIrCodes().size() - 1);
            if (irCode instanceof Jump) {
                flowGraph.addEdge(basicBlock1.getBlockNumber(), getLabelNumber(((Jump) irCode).getLabel()));
            } else if (irCode instanceof Branch) {
                flowGraph.addEdge(basicBlock1.getBlockNumber(), getLabelNumber(((Branch) irCode).getLabel()));
                flowGraph.addEdge(basicBlock1.getBlockNumber(), basicBlock1.getBlockNumber() + 1);
            } else if (!(irCode instanceof FuncEnd)) {
                flowGraph.addEdge(basicBlock1.getBlockNumber(), basicBlock1.getBlockNumber() + 1);
            }
        }
//        System.out.println(flowGraph.getBasicBlocks().size());
//        for (ArrayList<Integer> arrayList : flowGraph.getEdges()) {
//            System.out.println(arrayList);
//        }
    }

    private int getLabelNumber(String label) {
        for (IRCode irCode : getIrCodes()) {
            if (irCode instanceof Label && ((Label) irCode).getLabel().equals(label))
                return irCode.getBlockNumber();
        }
        return -1;
    }
}
