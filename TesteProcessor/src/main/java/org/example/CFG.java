package org.example;

import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.reflect.code.CtIfImpl;

import java.util.ArrayList;
import java.util.List;

public class CFG {

    private List<BasicBlock> basicBlocks;

    private List<GraphNode> allNodes;

    private List<GraphNode> lastNodesFromConditionalBranches;

    private String MethodName;

    public CFG(CtMethod method){

        basicBlocks = new ArrayList<>();
        //basicBlocks.add(new BasicBlock(currentId));
        allNodes = new ArrayList<>();
        lastNodesFromConditionalBranches = new ArrayList<>();

        getBasicBlocksFromCtBlock(basicBlocks, method.getBody(), allNodes, false);

        for (BasicBlock basicBlock : basicBlocks){
            basicBlock.getReachableBlocksFromEdges();
        }
    }

    public void getBasicBlocksFromCtBlock(List<BasicBlock> basicBlockList, CtBlock block,  List<GraphNode> allNodes, boolean isRecursive){
        if(block == null){
            return;
        }
        List<CtStatement> statementList = getStatementsInOrderFromBlock(block);
        BasicBlock newBasicBlock = new BasicBlock();
        basicBlockList.add(newBasicBlock);

        List<BasicBlock> basicBlockListForThisBlock = new ArrayList<>();
        basicBlockListForThisBlock.add(newBasicBlock);
        int numberOfBasicBlocksForThisBlock = 1;
        int numberOfStatementsForThisBlock = 0;
        boolean isAnyStatementNeedingEdge = false;

        for (CtStatement statement : statementList){
            numberOfStatementsForThisBlock++;
            GraphNode newNode = new GraphNode(statement);
            newNode.setBasicBlock(basicBlockListForThisBlock.get(numberOfBasicBlocksForThisBlock - 1));
            allNodes.add(newNode);
            newNode.setId(allNodes.size());

            GraphEdgeNode outgoingEdge = new GraphEdgeNode();
            if(statement instanceof CtIfImpl){
                int idFromPreviousStatement = allNodes.size() - 2;
                outgoingEdge.setDst(newNode);
                outgoingEdge.setSrc(allNodes.get( idFromPreviousStatement ) );
                allNodes.get(idFromPreviousStatement).setOutgoingEdges(outgoingEdge);

                getBasicBlocksFromCtBlock(basicBlockList, ((CtIfImpl) statement).getThenStatement(), allNodes, true);

                addLastNodesFromCodiditionalBranch();

                if( ( ( CtIfImpl ) statement).getElseStatement() != null){
                    int idFromFirstStatementFalseBranch = allNodes.size();
                    getBasicBlocksFromCtBlock(basicBlockList, ((CtIfImpl) statement).getElseStatement(), allNodes, true);

                    GraphEdgeNode falseBranchOutgoingEdge = new GraphEdgeNode();
                    falseBranchOutgoingEdge.setDst(allNodes.get( idFromFirstStatementFalseBranch ));
                    falseBranchOutgoingEdge.setSrc(newNode);
                    newNode.setOutgoingEdges(falseBranchOutgoingEdge);
                }
                else{
                    int idFromIfStatementWithoutElseStatement = idFromPreviousStatement + 1;
                    lastNodesFromConditionalBranches.add(allNodes.get(idFromIfStatementWithoutElseStatement));
                }


                isAnyStatementNeedingEdge = true;


                deleteOutgoingEdgesFromConditionalBranches();
                if(numberOfStatementsForThisBlock < statementList.size()){
                    numberOfBasicBlocksForThisBlock++;
                    basicBlockListForThisBlock.add(new BasicBlock());
                    basicBlockList.add(basicBlockListForThisBlock.get(numberOfBasicBlocksForThisBlock - 1));
                }
            }
            else{
                outgoingEdge.setDst(newNode);
                if (numberOfStatementsForThisBlock > 1 || isRecursive == true) {
                    int SrcId = allNodes.size() - 2;
                    outgoingEdge.setSrc( allNodes.get( SrcId ) );
                    allNodes.get( SrcId ).setOutgoingEdges( outgoingEdge );
                    if( isAnyStatementNeedingEdge ) {
                        connectLastNodesFromConditionalBranch(newNode);

                        lastNodesFromConditionalBranches.clear();

                        isAnyStatementNeedingEdge = false;
                    }
                }

            }
        }
    }

    public List<CtStatement> getStatementsInOrderFromBlock(CtBlock block){
        List<CtStatement> statementList = new ArrayList<>();
        for(CtStatement statement : block.getElements(new TypeFilter<>(CtStatement.class))){
            if(statement.getParent() == block){
                statementList.add(statement);
            }
        }

        return statementList;
    }

    public void ProcessIfStatement(List<BasicBlock>basicBlockList, CtIf ctIf, List<CtStatement> statementsInOrder){
    }

    public void printNodesFromBasicBlocks(){
        for(BasicBlock basicBlock : basicBlocks){
            System.out.println(basicBlock.getId());
            basicBlock.printNodes();
            System.out.println("------");
        }
    }

    public List<GraphNode> getAllNodes(){
        return this.allNodes;
    }

    public List<BasicBlock> getBasicBlocks(){
        return this.basicBlocks;
    }

    private void deleteOutgoingEdgesFromConditionalBranches(){
        for(GraphNode node : lastNodesFromConditionalBranches ){
            if (node.getStatement() instanceof CtIfImpl){
                continue;
            }
            node.deleteOutgoingEdge();

            node.getBasicBlock().deleteOutGoingEdges();
        }
    }

    private void connectLastNodesFromConditionalBranch(GraphNode newNode){
        for (GraphNode node : lastNodesFromConditionalBranches) {
            GraphEdgeNode outgoingEdgeConditionalStatement = new GraphEdgeNode();
            outgoingEdgeConditionalStatement.setDst(newNode);
            outgoingEdgeConditionalStatement.setSrc(node);

            node.setOutgoingEdges(outgoingEdgeConditionalStatement);
        }
    }

    private void addLastNodesFromCodiditionalBranch(){
        int idFromLastStatementTrueBranch = allNodes.size() - 1;
        if( !lastNodesFromConditionalBranches.contains(allNodes.get(idFromLastStatementTrueBranch))) {
            lastNodesFromConditionalBranches.add(allNodes.get(idFromLastStatementTrueBranch));
        }
    }
}
