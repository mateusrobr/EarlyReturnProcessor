package org.example;

import java.util.ArrayList;
import java.util.List;

public class PDGSlice {
    GraphNode localVariable;
    List<BasicBlock> boundaryBlockRaw;

    List<BasicBlock> boundaryBlockIntersection;

    List<BasicBlock> boundaryBlockUnion;

    public PDGSlice(GraphNode localVariable, List<BasicBlock> completeComputationBasicBlocks){
        this.localVariable = localVariable;
        this.boundaryBlockRaw = completeComputationBasicBlocks;
    }

    public List<BasicBlock> getBoundaryBlockIntersectionFromBoundaryBlockRaw(){
        List<BasicBlock> boundaryBlockIntersection = new ArrayList<>();
        for(int i =0; i < boundaryBlockRaw.size(); i++) {
            if (boundaryBlockRaw.lastIndexOf(boundaryBlockRaw.get(i)) != i)  {
                boundaryBlockIntersection.add(boundaryBlockRaw.get(i));
            }
        }
        return boundaryBlockIntersection;
    }

    public void printSlice(){
        System.out.println("Local Variable: " + localVariable);
        System.out.println("Blocks " + boundaryBlockRaw);
    }
}
