package midend.optimization;


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
    public void optimize(){
        for (BasicBlock basicBlock : basicBlocks) {
            basicBlock.optimize();
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

    public ArrayList<IRCode> generateIRCodes() {
        ArrayList<IRCode> irCodes = new ArrayList<>();
        for (BasicBlock basicBlock : basicBlocks) {
            if (visited.get(basicBlock.getBlockNumber())) {
                irCodes.addAll(basicBlock.getIrCodes());
            }
        }
        return irCodes;
    }
}
