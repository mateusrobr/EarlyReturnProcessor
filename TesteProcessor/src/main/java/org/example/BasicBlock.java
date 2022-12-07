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
    private GraphNode leader;
    private List<GraphNode> nodes;
    private Set<GraphEdge> incomingEdges;
    private Set<GraphEdge> outgoingEdges;

    public BasicBlock(int id){
        incomingEdges = new HashSet<>();
        outgoingEdges = new HashSet<>();
        nodes = new ArrayList<>();
        this.id = id;
    }


    public void addNode(GraphNode newNode){
        nodes.add(newNode);
    }

    public void printNodes(){
        System.out.println(nodes);
    }

    public void setIncomingEdges(){

    }
    public void setOutgoingEdges(List<GraphEdge> graphEdges){
        for(GraphEdge edge : graphEdges){
            outgoingEdges.add(edge);
        }
    }
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }

    public List<GraphNode> getNodes(){
        return this.nodes;
    }

}
