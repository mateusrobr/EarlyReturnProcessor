package org.example;

import spoon.Launcher;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.reflect.code.CtIfImpl;

import java.util.*;

public class PDGSlice {
    static private int totalNewMethods = 0;
    private GraphNode localVariable;
    private List<BasicBlock> boundaryBlockCompleteComputation;

    private  Map<BasicBlock, List<BasicBlock>> mapAux;

    private Launcher launcher;
    private BasicBlock region;

    public PDGSlice(GraphNode localVariable, List<BasicBlock> completeComputationBasicBlocks,Launcher launcher){
        this.localVariable = localVariable;
        this.boundaryBlockCompleteComputation = completeComputationBasicBlocks;
        this.launcher = launcher;
        mapAux = new LinkedHashMap<>();
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

    public CtBlock getCtBlockFromBasicBlock(BasicBlock baseRegion){
        CtBlock newCtBlock = this.launcher.getFactory().createBlock();
        for(GraphNode node : baseRegion.getNodes()) {
            if (node.getStatement() instanceof CtIfImpl) {
                CtIf nodeIfStatement = (CtIf) node.getStatement();
                CtIf ifStatement = this.launcher.getFactory().createIf();
                ifStatement.setCondition(nodeIfStatement.getCondition());
                newCtBlock.addStatement(ifStatement);
                if(mapAux.get(baseRegion).size() > 0){
                    for(BasicBlock block : mapAux.get(baseRegion)){
                        if(baseRegion.getOutgoingEdges().get(0).getDst().equals(block)){
                            ifStatement.setThenStatement(getCtBlockFromBasicBlock(block));

                        } else if (baseRegion.getOutgoingEdges().get(1).getDst().equals(block) ) {
                            if(baseRegion.getOutgoingEdges().get(1).getIsControlEdgeCFG()){
                                ifStatement.setElseStatement(getCtBlockFromBasicBlock(block));
                            }
                            else{
                                CtStatementList list = getCtBlockFromBasicBlock(block);
                                for(CtStatement statement : list){
                                    newCtBlock.addStatement(statement.clone());
                                }
                            }
                        }else{
                            CtStatementList list = getCtBlockFromBasicBlock(block);
                            for(CtStatement statement : list){
                                newCtBlock.addStatement(statement.clone());
                            }
                        }
                    }
                }



            } else {
                newCtBlock.addStatement(node.getStatement().clone());
            }
        }

        return newCtBlock;
    }

    public CtMethod produceNewMethod() {
        totalNewMethods++;
        CtMethod newMethod = this.launcher.getFactory().createMethod();
        CtLocalVariable localVariableaux = (CtLocalVariable) localVariable.getStatement();
        newMethod.setType(localVariableaux.getType());
        newMethod.setSimpleName("newMethod" + totalNewMethods);

        for(BasicBlock completeComputationBlock : boundaryBlockCompleteComputation){
            List<BasicBlock> listAux = new ArrayList<>();
            for(BasicBlock blocks: boundaryBlockCompleteComputation){
                if(blocks.getControlDependent() != null){
                    if(blocks.getControlDependent().getId() == completeComputationBlock.getId()){
                        listAux.add(blocks);
                    }
                }
                else {
                    if(blocks.getId() != completeComputationBlock.getId() && blocks.getId() > completeComputationBlock.getId() && completeComputationBlock.getControlDependent() == null){
                        if(mapAux.size() > 0){

                        }
                        else{
                            listAux.add(blocks);
                        }
                    }
                }
            }
            this.mapAux.put(completeComputationBlock,listAux);
        }
        if(boundaryBlockCompleteComputation.size() < 1){
            return newMethod;
        }
        newMethod.setBody(getCtBlockFromBasicBlock(boundaryBlockCompleteComputation.get(0)));

        return newMethod;
    }
    public Map<BasicBlock, List<BasicBlock>> getMapAux(){
        return this.mapAux;
    }

    public void printSlice(){
        System.out.println("Local Variable: " + localVariable);
        System.out.println("Blocks " + boundaryBlockCompleteComputation);
        //System.out.println("Regio " + region);
    }
}
