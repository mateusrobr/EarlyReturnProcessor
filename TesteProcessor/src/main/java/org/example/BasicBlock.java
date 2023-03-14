package org.example;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BasicBlock {
    private int id;

    private static int BasicBlockNum = 0;
    private GraphNode leader;
    private List<GraphNode> nodes;
    private Set<GraphEdgeBasicBlock> incomingEdges;
    private Set<GraphEdgeBasicBlock> outgoingEdges;

    public BasicBlock(){
        incomingEdges = new HashSet<>();
        outgoingEdges = new HashSet<>();
        nodes = new ArrayList<>();
        ++BasicBlockNum;
        this.id = BasicBlockNum;
    }


    public void addNode(GraphNode newNode){
        nodes.add(newNode);
    }

    public void printNodes(){
        System.out.println(nodes);
    }

    public void setIncomingEdges(){

    }
    public void setOutgoingEdges(GraphEdgeBasicBlock outgoingEdge){
        this.outgoingEdges.add(outgoingEdge);
    }

    public Set<GraphEdgeBasicBlock> getOutgoingEdges(){
        return this.outgoingEdges;
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

    public String toString(){
        return "id: " + id;
    }

}
