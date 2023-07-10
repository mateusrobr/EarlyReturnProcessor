package org.example;

import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtParameter;
import spoon.support.reflect.code.CtIfImpl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class
GraphNode {

    private CtStatement statement;
    private Set<GraphEdgeNode> incomingEdges;
    private Set<GraphEdgeNode> outgoingEdges;

    private List<GraphEdgeNode> dataDependenceLocalStatements;

    private List<CtParameter> dataDependenceParameters;
    private BasicBlock basicBlock;

    private boolean isLeader;

    private int id;

    private String name;

    public GraphNode(CtStatement statement){
        this.statement = statement;
        incomingEdges = new LinkedHashSet<GraphEdgeNode>();
        outgoingEdges = new LinkedHashSet<GraphEdgeNode>();
        dataDependenceLocalStatements = new ArrayList<>();
        dataDependenceParameters = new ArrayList<>();
    }

    public void setIncomingEdge(GraphEdgeNode incomingEdge){
        incomingEdges.add(incomingEdge);

        GraphEdgeNode outgoingEdge = new GraphEdgeNode();
        outgoingEdge.setSrc(incomingEdge.getDst());
        outgoingEdge.setDst(incomingEdge.getSrc());
        //setOutgoingEdges(outgoingEdge);
    }

    public void setOutgoingEdges(GraphEdgeNode outgoingEdge, boolean isControlEdgeCFG){
        //outgoingEdges.add(outgoingEdge);
        if(isControlEdgeCFG){
            outgoingEdge.setIsControlEdge(true);
        }
        if(outgoingEdge.getDst().getBasicBlock() != outgoingEdge.getSrc().getBasicBlock()){
            GraphEdgeBasicBlock newEdge = new GraphEdgeBasicBlock();
            newEdge.setSrc(outgoingEdge.getSrc().getBasicBlock());
            newEdge.setDst(outgoingEdge.getDst().getBasicBlock());

            outgoingEdge.getSrc().getBasicBlock().setOutgoingEdges(newEdge);

        }
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

    public void setName(String newName){
        this.name = newName;
    }

    public int getId(){
        return id;
    }

    public String toString(){
        if(statement instanceof CtIfImpl){
            return "Statement: " + id + " if( " +((CtIfImpl) statement).getCondition() + " )";
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
    public void setDataDependenceLocalStatements(GraphNode node){
        GraphEdgeNode newEdge = new GraphEdgeNode();
        newEdge.setSrc(this);
        newEdge.setDst(node);
        this.dataDependenceLocalStatements.add(newEdge);
    }
    public void setDataDependenceParameters(CtParameter parameter){
        dataDependenceParameters.add(parameter);
    }
    public List<CtParameter> getDataDependenceParameters(){
        return this.dataDependenceParameters;
    }
    public List<GraphEdgeNode> getDataDependenceLocalStatements(){
        return this.dataDependenceLocalStatements;
    }
    public BasicBlock getBasicBlock(){return this.basicBlock;}

    public CtStatement getStatement(){
        return this.statement;
    }
    public void deleteOutgoingEdge(){
        if(!outgoingEdges.isEmpty()){
            outgoingEdges.clear();
        }
    }

}
