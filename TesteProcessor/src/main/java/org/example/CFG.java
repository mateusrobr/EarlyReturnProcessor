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
        basicBlocks.add(new BasicBlock(currentId));
        allNodes = new ArrayList<>();

        getBasicBlocksFromCtBlock(basicBlocks, method.getBody(), currentId, allNodes);


    }

    public void getBasicBlocksFromCtBlock(List<BasicBlock> basicBlockList, CtBlock block, int id, List<GraphNode> allNodes){
        if(block == null){
            return;
        }
        List<CtStatement> statementList = getStatementsInOrderFromBlock(block);
        BasicBlock newBasicBlock = new BasicBlock(id++);
        basicBlockList.add(newBasicBlock);

        for (CtStatement statement : statementList){
            GraphNode newNode = new GraphNode(statement);
            newNode.setBasicBlock(newBasicBlock);
            allNodes.add(newNode);

            if(statement instanceof CtIfImpl){

                getBasicBlocksFromCtBlock(basicBlockList, ((CtIfImpl) statement).getThenStatement(), id, allNodes);
                id++;
                getBasicBlocksFromCtBlock(basicBlockList, ((CtIfImpl) statement).getElseStatement(), ++id, allNodes);
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
}
