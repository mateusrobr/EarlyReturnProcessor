package org.example;


import spoon.reflect.code.CtStatement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class BasicBlock {
    private int id;

    private static int BasicBlockNum = 0;
    private GraphNode leader;
    private List<GraphNode> nodes;
    private Set<GraphEdgeBasicBlock> incomingEdges;
    private List<GraphEdgeBasicBlock> outgoingEdges;

    private List<BasicBlock> reachableBlocks;
    private List<BasicBlock> dominatedBlocks;

    public BasicBlock(){
        incomingEdges = new HashSet<>();
        outgoingEdges = new ArrayList<>();
        nodes = new ArrayList<>();
        ++BasicBlockNum;
        this.id = BasicBlockNum;
        reachableBlocks = new ArrayList<>();
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
        boolean isDuplicatedEdge = false;
        for(GraphEdgeBasicBlock graphEdgeBasicBlock : outgoingEdges){
            if(graphEdgeBasicBlock.getDst().getId() == outgoingEdge.getDst().getId()){
                isDuplicatedEdge = true;
                break;
            }
        }
        if(!isDuplicatedEdge){
            this.outgoingEdges.add(outgoingEdge);
        }
    }

    public List<GraphEdgeBasicBlock> getOutgoingEdges(){
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

    public void getReachableBlocksFromEdges(){

        for(int i = 0 ; i < outgoingEdges.size() ; i++){
            if(!isDuplicatedInReachableBlocks(outgoingEdges.get(i))){
                reachableBlocks.add(outgoingEdges.get(i).getDst());
                System.out.println("A");
                outgoingEdges.get(i).getDst().getReachableBlocksFromEdges();
            }
        }

        getReachableBlocksFromDirectReachableBlock();

    }

    private void getReachableBlocksFromDirectReachableBlock(){
        for(int i = 0 ; i < reachableBlocks.size() ; i++){
            addReachableBlocks(reachableBlocks.get(i).getReachableBlocks());
        }
    }
    private boolean isDuplicatedInReachableBlocks(GraphEdgeBasicBlock outgoingEdge){
        boolean isDuplicatedEdge = false;
        for (BasicBlock reachBlocks : reachableBlocks){
            if(outgoingEdge.getDst().getId() == reachBlocks.getId()){
                isDuplicatedEdge = true;
                return isDuplicatedEdge;
            }
        }

        return isDuplicatedEdge;
    }
    private boolean isDuplicatedInReachableBlocks(BasicBlock basicBlock){
        boolean isDuplicatedBasicBlock = false;
        for(BasicBlock reachBlocks : reachableBlocks){
            if (reachBlocks.getId() == basicBlock.getId()){
                isDuplicatedBasicBlock = true;
                return isDuplicatedBasicBlock;
            }
        }

        return isDuplicatedBasicBlock;
    }
    private void addReachableBlocks(List<BasicBlock> childrenOfDirectReachableBlocks){

        for(BasicBlock basicBlock : childrenOfDirectReachableBlocks){
            if(!isDuplicatedInReachableBlocks(basicBlock)){
                this.reachableBlocks.add(basicBlock);
            }
        }
        //this.reachableBlocks.addAll(childrenOfDirectReachableBlocks);
    }

    public List<BasicBlock> getReachableBlocks() {
        return reachableBlocks;
    }

    public String toString(){
        return "id: " + id;
    }

    public void deleteOutGoingEdges(){
        outgoingEdges.clear();
    }

}
