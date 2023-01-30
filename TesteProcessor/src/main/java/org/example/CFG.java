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

    private String MethodName;

    public CFG(CtMethod method){
        int currentId = 1;
        basicBlocks = new ArrayList<>();
        //basicBlocks.add(new BasicBlock(currentId));
        allNodes = new ArrayList<>();

        getBasicBlocksFromCtBlock(basicBlocks, method.getBody(), currentId, allNodes, false);


    }

    public void getBasicBlocksFromCtBlock(List<BasicBlock> basicBlockList, CtBlock block, int blockId, List<GraphNode> allNodes, boolean isRecursive){
        if(block == null){
            return;
        }
        List<CtStatement> statementList = getStatementsInOrderFromBlock(block);
        BasicBlock newBasicBlock = new BasicBlock(blockId++);
        basicBlockList.add(newBasicBlock);

        List<BasicBlock> basicBlockListForThisBlock = new ArrayList<>();
        basicBlockListForThisBlock.add(newBasicBlock);

        int numberOfBasicBlocksForThisBlock = 1;
        int numberOfStatementsForThisBlock = 0;

        for (CtStatement statement : statementList){
            numberOfStatementsForThisBlock++;
            GraphNode newNode = new GraphNode(statement);
            newNode.setBasicBlock(basicBlockListForThisBlock.get(numberOfBasicBlocksForThisBlock - 1));
            allNodes.add(newNode);
            newNode.setId(allNodes.size());

            GraphEdge outgoingEdge = new GraphEdge();

            if(statement instanceof CtIfImpl){

                int idFromPreviousStatement = allNodes.size() - 2;
                //int idFromThisStatement = allNodes.size() - 1;
                outgoingEdge.setDst(newNode);
                outgoingEdge.setSrc(allNodes.get( idFromPreviousStatement ) );
                allNodes.get(idFromPreviousStatement).setOutgoingEdges(outgoingEdge);

                getBasicBlocksFromCtBlock(basicBlockList, ((CtIfImpl) statement).getThenStatement(), blockId, allNodes, true);
                blockId++;

                //GraphEdge trueBranhOutgoingEdge = new GraphEdge();
                //trueBranhOutgoingEdge.setSrc(newNode);
                //;newNode.setOutgoingEdges(trueBranhOutgoingEdge);


                if( ( ( CtIfImpl ) statement).getElseStatement() != null){
                    int idFromTheFirstStatementOfElseStatement = allNodes.size();
                    getBasicBlocksFromCtBlock(basicBlockList, ((CtIfImpl) statement).getElseStatement(), ++blockId, allNodes, true);
                    blockId++;


                    GraphEdge falseBranchOutgoingEdge = new GraphEdge();
                    falseBranchOutgoingEdge.setDst(allNodes.get( idFromTheFirstStatementOfElseStatement ));
                    falseBranchOutgoingEdge.setSrc(newNode);
                    newNode.setOutgoingEdges(falseBranchOutgoingEdge);

                }

                if(numberOfStatementsForThisBlock < statementList.size()){
                    numberOfBasicBlocksForThisBlock++;
                    basicBlockListForThisBlock.add(new BasicBlock(blockId++));
                    basicBlockList.add(basicBlockListForThisBlock.get(numberOfBasicBlocksForThisBlock - 1));
                }



            }
            else{
                outgoingEdge.setDst(newNode);
                /*if(numberOfStatementsForThisBlock == statementList.size() && isRecursive == true){
                     //do nothing
                }*/
                if (numberOfStatementsForThisBlock > 1 || isRecursive == true) {
                    int SrcId = allNodes.size() - 2;
                    outgoingEdge.setSrc(allNodes.get(SrcId));
                    allNodes.get(SrcId).setOutgoingEdges(outgoingEdge);
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
}
