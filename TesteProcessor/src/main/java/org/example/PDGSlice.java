package org.example;

import spoon.Launcher;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.declaration.CtMethod;

import java.util.ArrayList;
import java.util.List;

public class PDGSlice {
    GraphNode localVariable;
    List<BasicBlock> boundaryBlockCompleteComputation;

    BasicBlock region;

    public PDGSlice(GraphNode localVariable, List<BasicBlock> completeComputationBasicBlocks){
        this.localVariable = localVariable;
        this.boundaryBlockCompleteComputation = completeComputationBasicBlocks;
        //this.region = region;
    }

    public List<BasicBlock> getBoundaryBlockIntersectionFromBoundaryBlockRaw(){
        List<BasicBlock> boundaryBlockIntersection = new ArrayList<>();
        for(int i = 0; i < boundaryBlockCompleteComputation.size(); i++) {
            if (boundaryBlockCompleteComputation.lastIndexOf(boundaryBlockCompleteComputation.get(i)) != i)  {
                boundaryBlockIntersection.add(boundaryBlockCompleteComputation.get(i));
            }
        }
        return boundaryBlockIntersection;
    }

    public CtMethod produceNewMethod(Launcher launcher){
        CtMethod newMethod = launcher.getFactory().createMethod();
        CtLocalVariable localVariableaux = (CtLocalVariable) localVariable.getStatement();
        System.out.println(localVariableaux.getType());
        return newMethod;
    }

    public void printSlice(){
        System.out.println("Local Variable: " + localVariable);
        System.out.println("Blocks " + boundaryBlockCompleteComputation);
        System.out.println("Regio " + region);
    }
}
