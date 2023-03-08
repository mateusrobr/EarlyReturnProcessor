package org.example;

import spoon.reflect.code.CtStatement;
import spoon.support.reflect.code.CtIfImpl;

import java.util.LinkedHashSet;
import java.util.Set;

public class
GraphNode {

    private CtStatement statement;
    private Set<GraphEdgeNode> incomingEdges;
    private Set<GraphEdgeNode> outgoingEdges;

    private BasicBlock basicBlock;

    private boolean isLeader;

    private int id;

    public GraphNode(CtStatement statement){
        this.statement = statement;
        incomingEdges = new LinkedHashSet<GraphEdgeNode>();
        outgoingEdges = new LinkedHashSet<GraphEdgeNode>();
    }

    public void setIncomingEdge(GraphEdgeNode incomingEdge){
        incomingEdges.add(incomingEdge);
        if(incomingEdge.getDst().getBasicBlock() != incomingEdge.getSrc().getBasicBlock()){

        }

        GraphEdgeNode outgoingEdge = new GraphEdgeNode();
        outgoingEdge.setSrc(incomingEdge.getDst());
        outgoingEdge.setDst(incomingEdge.getSrc());
        setOutgoingEdges(outgoingEdge);
    }

    public void setOutgoingEdges(GraphEdgeNode outgoingEdge){
        outgoingEdges.add(outgoingEdge);
    }

    public Set<GraphEdgeNode> getIncomingEdges(){
        return incomingEdges;
    }

    public Set<GraphEdgeNode> getOutgoingEdges(){
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
    public BasicBlock getBasicBlock(){return this.basicBlock;}
    public void deleteOutgoingEdge(){
        if(!outgoingEdges.isEmpty()){
            outgoingEdges.clear();
        }
    }

}
