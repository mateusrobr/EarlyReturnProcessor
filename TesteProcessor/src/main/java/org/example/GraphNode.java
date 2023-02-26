package org.example;

import spoon.reflect.code.CtStatement;
import spoon.support.reflect.code.CtIfImpl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GraphNode {

    private CtStatement statement;
    private Set<GraphEdge> incomingEdges;
    private Set<GraphEdge> outgoingEdges;

    private BasicBlock basicBlock;

    private boolean isLeader;

    private int id;

    public GraphNode(CtStatement statement){
        this.statement = statement;
        incomingEdges = new LinkedHashSet<GraphEdge>();
        outgoingEdges = new LinkedHashSet<GraphEdge>();
    }

    public void setIncomingEdge(GraphEdge incomingEdge){
        incomingEdges.add(incomingEdge);
        GraphEdge outgoingEdge = new GraphEdge();
        outgoingEdge.setSrc(incomingEdge.getDst());
        outgoingEdge.setDst(incomingEdge.getSrc());
        setOutgoingEdges(outgoingEdge);
    }

    public void setOutgoingEdges(GraphEdge outgoingEdge){
        outgoingEdges.add(outgoingEdge);
    }

    public Set<GraphEdge> getIncomingEdges(){
        return incomingEdges;
    }

    public Set<GraphEdge> getOutgoingEdges(){
        return outgoingEdges;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public String toString(){
        if(statement instanceof CtIfImpl){
            return "Statement: " + "if( " +((CtIfImpl) statement).getCondition() + " )";
        }
        return "Statement: " + id + " " + statement.prettyprint();
    }

    public boolean isLeader(){
        return isLeader;
    }

    public void setBasicBlock(BasicBlock basicBlock){
        this.basicBlock = basicBlock;
        basicBlock.addNode(this);
    }

    public void deleteOutgoingEdge(){
        if(!outgoingEdges.isEmpty()){
            outgoingEdges.clear();
        }
    }

}
