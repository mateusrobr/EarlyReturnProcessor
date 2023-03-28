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
        reachableBlocks.add(this);
        dominatedBlocks = new ArrayList<>();
        dominatedBlocks.add(this);
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
        //reachableBlocks.add(this);
        for(int i = 0 ; i < outgoingEdges.size() ; i++){
            if(!isBasicBlockInListAndAdd(outgoingEdges.get(i),reachableBlocks)){
                //reachableBlocks.add(outgoingEdges.get(i).getDst());
               // System.out.println("A");
                outgoingEdges.get(i).getDst().getReachableBlocksFromEdges();
            }
        }

        getReachableBlocksFromDirectReachableBlock();

    }
    public void getDominatedBlocksFromEdges(){
        //dominatedBlocks.add(this);
        if(outgoingEdges.size() > 1){
            for(int i = 0 ; i < outgoingEdges.size() ; i++){
                if(!isBasicBlockInListAndAdd(outgoingEdges.get(i), dominatedBlocks)){
                    outgoingEdges.get(i).getDst().getDominatedBlocksFromEdges();
                }
            }
        }
        getDominatedBlocksFromDirectDominatedBlocks();
    }

    private void getReachableBlocksFromDirectReachableBlock(){
        for(int i = 0 ; i < reachableBlocks.size() ; i++){
            addReachableBlocks(reachableBlocks.get(i).getReachableBlocks());
        }
    }
    private void getDominatedBlocksFromDirectDominatedBlocks(){
        for (int i = 0 ; i < dominatedBlocks.size() ; i++){
            addDominatedBlocks(dominatedBlocks.get(i).getDominatedBlocks());
        }
    }
    private boolean isBasicBlockInListAndAdd(GraphEdgeBasicBlock outgoingEdge, List<BasicBlock> basicBlockList){
        boolean isDuplicatedEdge = false;
        for (BasicBlock reachBlocks : basicBlockList){
            if(outgoingEdge.getDst().getId() == reachBlocks.getId()){
                isDuplicatedEdge = true;
                return isDuplicatedEdge;
            }
        }
        basicBlockList.add(outgoingEdge.getDst());
        return isDuplicatedEdge;
    }
    private boolean isBasicBlockAlreadyInList(BasicBlock basicBlock,List<BasicBlock> basicBlockList){
        boolean isDuplicatedBasicBlock = false;
        for(BasicBlock reachBlocks : basicBlockList){
            if (reachBlocks.getId() == basicBlock.getId()){
                isDuplicatedBasicBlock = true;
                return isDuplicatedBasicBlock;
            }
        }

        return isDuplicatedBasicBlock;
    }
    private void addReachableBlocks(List<BasicBlock> childrenOfDirectReachableBlocks){

        for(BasicBlock basicBlock : childrenOfDirectReachableBlocks){
            if(!isBasicBlockAlreadyInList(basicBlock,reachableBlocks)){
                this.reachableBlocks.add(basicBlock);
            }
        }
        //this.reachableBlocks.addAll(childrenOfDirectReachableBlocks);
    }
    private void addDominatedBlocks(List<BasicBlock> childrenOfDirectEdges){
        for(BasicBlock basicBlock : childrenOfDirectEdges){
            if(!isBasicBlockAlreadyInList(basicBlock, dominatedBlocks)){
                this.dominatedBlocks.add(basicBlock);
            }
        }
    }

    public List<BasicBlock> getReachableBlocks() {
        return reachableBlocks;
    }

    public List<BasicBlock> getDominatedBlocks() {
        return dominatedBlocks;
    }

    public String toString(){
        return "id: " + id;
    }

    public void deleteOutGoingEdges(){
        outgoingEdges.clear();
    }

}
