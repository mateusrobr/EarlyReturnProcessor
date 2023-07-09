package org.example;

import spoon.Launcher;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.support.reflect.code.CtIfImpl;

import java.util.ArrayList;
import java.util.List;

public class PDGSlice {
    static private int totalNewMethods = 0;
    private GraphNode localVariable;
    private List<BasicBlock> boundaryBlockCompleteComputation;

    private List<BasicBlock> alreadyVisited;

    private Launcher launcher;
    private BasicBlock region;

    public PDGSlice(GraphNode localVariable, List<BasicBlock> completeComputationBasicBlocks,Launcher launcher){
        this.localVariable = localVariable;
        this.boundaryBlockCompleteComputation = completeComputationBasicBlocks;
        this.launcher = launcher;
        //this.region = region;
    }

    public List<BasicBlock> getBoundaryBlockIntersectionFromBoundaryBlockRaw(){
        List<BasicBlock> boundaryBlockIntersection = new ArrayList<>();
        for(int i = 0; i < boundaryBlockCompleteComputation.size(); i++) {
            if (boundaryBlockCompleteComputation.lastIndexOf(boundaryBlockCompleteComputation.get(i)) != i)  {
                boundaryBlockIntersection.add(boundaryBlockCompleteComputation.get(i));
            }
        }
        return boundaryBlockIntersection;
    }
    public List<CtParameter> getParametersForNewMethod(){
        List<CtParameter> parameterList = new ArrayList<>();

        return parameterList;
    }
    public CtBlock getIfElseCtBlocks(GraphNode node){
        CtBlock newCtBlock = this.launcher.getFactory().createBlock();


        return newCtBlock;
    }
//    public CtBlock getCtBlockFromBasicBlock(BasicBlock block){
//        CtBlock newCtBlock = this.launcher.getFactory().createBlock();
//        //alreadyVisited.add(block);
//
//        for(GraphNode node : block.getNodes()){
//            if(node.getStatement() instanceof CtIfImpl){
//                CtIf nodeIfStatement = (CtIf) node.getStatement();
//                CtIf ifStatement = this.launcher.getFactory().createIf();
//                ifStatement.setCondition(nodeIfStatement.getCondition());
//                List<GraphEdgeNode> edges = new ArrayList<>(node.getOutgoingEdges());
//                if(edges.size() > 0){
//                    ifStatement.setThenStatement(getCtBlockFromBasicBlock(edges.get(0).getDst().getBasicBlock()));
//                }
//                if(edges.size() > 1){
//                    ifStatement.setElseStatement(getCtBlockFromBasicBlock(edges.get(1).getDst().getBasicBlock()));
//                }
//                //ifStatement.setThenStatement(getCtBlockFromBasicBlock(edges.get(0).getDst().getBasicBlock()));
//                newCtBlock.addStatement(ifStatement);
//
////                for(GraphEdgeNode edge : node.getOutgoingEdges()){
////
////                }
//                //CtBlock ifElseBlock = getIfElseCtBlocks();
//
//            }
//            else{
//            newCtBlock.addStatement(node.getStatement().clone());
//            }
//        }
//
//        return newCtBlock;
//    }

    public CtBlock getCtBlockFromBasicBlock(BasicBlock block){
        CtBlock newCtBlock = this.launcher.getFactory().createBlock();
        //alreadyVisited.add(block);
        for(GraphNode node : block.getNodes()) {
            if (node.getStatement() instanceof CtIfImpl) {
                CtIf nodeIfStatement = (CtIf) node.getStatement();
                CtIf ifStatement = this.launcher.getFactory().createIf();
                ifStatement.setCondition(nodeIfStatement.getCondition());
                newCtBlock.addStatement(ifStatement);

            } else {
                newCtBlock.addStatement(node.getStatement().clone());
            }
        }

        return newCtBlock;
    }

    public CtMethod produceNewMethod(){
        totalNewMethods++;
        List<CtBlock> blockList = new ArrayList<>();
        CtMethod newMethod = this.launcher.getFactory().createMethod();
        CtLocalVariable localVariableaux = (CtLocalVariable) localVariable.getStatement();
        newMethod.setType(localVariableaux.getType());
        newMethod.setSimpleName("newMethod" + totalNewMethods);
        for (int i = 0 ; i < boundaryBlockCompleteComputation.size() ; i++){
            blockList.add(getCtBlockFromBasicBlock(boundaryBlockCompleteComputation.get(i)));
        }
        System.out.println("Variable: " + localVariable);
        for (CtBlock block : blockList){
            System.out.println(block.prettyprint());
        }
        return newMethod;
    }

    public void printSlice(){
        System.out.println("Local Variable: " + localVariable);
        System.out.println("Blocks " + boundaryBlockCompleteComputation);
        //System.out.println("Regio " + region);
    }
}
