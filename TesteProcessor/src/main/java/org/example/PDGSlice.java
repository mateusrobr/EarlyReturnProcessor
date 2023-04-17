package org.example;

import java.util.List;
import java.util.Map;

public class PDGSlice {
    GraphNode localVariable;
    List<BasicBlock> boundaryBlock;

    public PDGSlice(GraphNode localVariable, List<BasicBlock> completeComputationBasicBlocks){
        this.localVariable = localVariable;
        this.boundaryBlock = completeComputationBasicBlocks;
    }
}
