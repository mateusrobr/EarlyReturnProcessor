package org.example;

import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtStatement;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.reflect.code.CtIfImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BasicBlock {
    private int id;
    List<GraphNode> nodes;
    Set<GraphEdge> incomingEdges;
    Set<GraphEdge> outgoingEdges;

    public BasicBlock(){
        incomingEdges = new HashSet<>();
        outgoingEdges = new HashSet<>();
        nodes = new ArrayList<>();
    }

    public void addNodesFromCtBlock(CtBlock block){

        for(CtStatement statement : block.getElements(new TypeFilter<>(CtStatement.class))){
            if(statement.getParent() == block){
                nodes.add(new GraphNode(statement));
            }
        }

    }

    public void printNodes(){
        System.out.println(nodes);
    }

}
