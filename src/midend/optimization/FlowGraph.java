package midend.optimization;


import midend.ir.FuncEnd;
import midend.ir.FuncEntry;
import midend.ir.IRCode;

import java.util.ArrayList;

public class FlowGraph {
    private ArrayList<BasicBlock> basicBlocks = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> edges = new ArrayList<>();
    private ArrayList<Boolean> visited = new ArrayList<>();

    public void addBasicBlock(BasicBlock basicBlock, boolean isVisited) {
        basicBlocks.add(basicBlock);
        edges.add(new ArrayList<>());
        visited.add(isVisited);
    }

    public ArrayList<BasicBlock> getBasicBlocks() {
        return basicBlocks;
    }

    public void optimize() {
        for (BasicBlock basicBlock : basicBlocks) {
            basicBlock.optimize();
        }
    }

    public void analyse() {
        ArrayList<Integer> queue = new ArrayList<>();
        int length = basicBlocks.size();
        for (int i = 0; i < length; i++) {
            basicBlocks.get(i).analyse();
            System.out.println(edges.get(i));
            queue.add(i);
        }
        while (!queue.isEmpty()) {
            int now = queue.get(0);
            queue.remove(0);
            BasicBlock basicBlock = basicBlocks.get(now);
            ArrayList<String> def = basicBlock.getDef(), use = basicBlock.getUse(), in = basicBlock.getIn(), out = basicBlock.getOut();
            ArrayList<String> temp = (ArrayList<String>) out.clone();
            temp.removeAll(def);
            temp.removeAll(use);
            temp.addAll(use);
            if (temp.size() != in.size()) {
                for (int i = 0; i < length; i++) {
                    if (edges.get(i).contains(now)) {
                        ArrayList<String> temp1 = basicBlocks.get(i).getOut();
                        boolean flag = false;
                        for (String s : temp) {
                            if (!temp1.contains(s)) {
                                temp1.add(s);
                                flag = true;
                            }
                        }
                        if (flag) {
                            queue.add(i);
                        }
                    }
                }
            }
            basicBlock.setIn(temp);
        }
        for (BasicBlock basicBlock : basicBlocks) {
//            basicBlock.print();
        }
    }

    public void addEdge(int from, int to) {
        assert to != -1;
        edges.get(from).add(to);
        visited.set(to, true);
    }

    public ArrayList<ArrayList<Integer>> getEdges() {
        return edges;
    }

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<IRCode> generateIRCodes() {
        ArrayList<IRCode> irCodes = new ArrayList<>();
        for (BasicBlock basicBlock : basicBlocks) {
            if (visited.get(basicBlock.getBlockNumber())) {
                irCodes.addAll(basicBlock.getIrCodes());
            }
        }
        if (name != null) {
            irCodes.add(0, new FuncEntry(name));
            irCodes.add(new FuncEnd(name));
        }
        return irCodes;
    }
}
