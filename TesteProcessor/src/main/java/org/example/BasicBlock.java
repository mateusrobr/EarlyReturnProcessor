package org.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BasicBlock {
    private int id;
    private GraphNode leader;
    private List<GraphNode> nodes;
    private Set<GraphEdgeNode> incomingEdges;
    private Set<GraphEdgeNode> outgoingEdges;

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
    public void setOutgoingEdges(List<GraphEdgeNode> graphEdgeNodes){
        for(GraphEdgeNode edge : graphEdgeNodes){
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
