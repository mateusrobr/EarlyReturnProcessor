package org.example;

import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtReference;
import spoon.reflect.visitor.chain.CtConsumer;
import spoon.reflect.visitor.filter.VariableReferenceFunction;
import spoon.support.reflect.code.CtAssignmentImpl;
import spoon.support.reflect.code.CtBlockImpl;
import spoon.support.reflect.code.CtLocalVariableImpl;

import java.util.*;
import java.util.stream.Collectors;

public class PDG {
    private CFG cfg;
    private List<GraphNode> allLocalVariablesForMethod;

    private Map<GraphNode, List<GraphNode>> localVariablesOcurrences;

    private Map<GraphNode, List<GraphNode>> localVariableAssigmentOcurrences;
    public PDG(CtMethod targetMethod){
        cfg = new CFG(targetMethod);
        allLocalVariablesForMethod = new ArrayList<>();
        this.fromGraphNodesGetLocalVariables();
        localVariablesOcurrences = getMapOfLocalVariableAndAllOcurrencesOfLocalVariable();
        localVariableAssigmentOcurrences = getStatementsLocalVariableIsAssigned();
    }

    private void fromGraphNodesGetLocalVariables(){
        for(GraphNode node : cfg.getAllNodes()){
            if (node.getStatement().getClass() == CtLocalVariableImpl.class){
                allLocalVariablesForMethod.add(node);
            }
        }
    }
    public void fromCFGGraphNodesGetAllOcurrencesOfALocalVariable(){
        getMapOfLocalVariableAndAllOcurrencesOfLocalVariable();

    }
    public Map<GraphNode, List<GraphNode>> getMapOfLocalVariableAndAllOcurrencesOfLocalVariable(){
        Map<GraphNode, List<GraphNode>> statementMap = new HashMap<>();
        List<List<GraphNode>> auxList = new ArrayList<>();
        List<CtStatement> allCFGCtStatements = cfg.getAllCtStatements();
        for(Map.Entry<GraphNode, CtStatement> entry : cfg.getMapGraphNodeCtStatement().entrySet()){
            auxList.add(new ArrayList<>());
            entry.getKey().getStatement().map(new VariableReferenceFunction()).forEach(new CtConsumer<CtReference>() {
                public void accept(CtReference t){
                    auxList.get(auxList.size() - 1).add(getGraphNodeFromCtReference(t, allCFGCtStatements));
                }
            });

            statementMap.put(entry.getKey(), auxList.get(auxList.size() - 1));
        }

        return statementMap;
    }
    public GraphNode getGraphNodeFromCtReference(CtReference reference, List<CtStatement> statementList){
        CtElement completeStatement =  reference.getParent();

        while(completeStatement.getParent().getClass() != CtBlockImpl.class){
            completeStatement = completeStatement.getParent();
        }
        statementList.indexOf((CtStatement) completeStatement);
        return cfg.getAllNodes().get(statementList.indexOf((CtStatement) completeStatement));
    }
    public Map<GraphNode, List<GraphNode>> getStatementsLocalVariableIsAssigned(){
        Map<GraphNode, List<GraphNode>> sliceCriteria = new HashMap<>();
        for (Map.Entry<GraphNode, List<GraphNode>> entry : localVariablesOcurrences.entrySet()){
            List<GraphNode> auxList = new ArrayList<>();
            CtLocalVariable localVariable = (CtLocalVariable) entry.getKey().getStatement();
            for (GraphNode node : entry.getValue()){
                if(node.getStatement() instanceof CtAssignmentImpl){
                    CtAssignment assignment = (CtAssignment) node.getStatement();
                    CtReference reference = (CtReference) assignment.getAssigned().getDirectChildren().get(0);
                    if(localVariable.getSimpleName().equals(reference.getSimpleName())){
                        auxList.add(node);
                    }
                }
            }
            sliceCriteria.put(entry.getKey(), auxList);
        }
        return sliceCriteria;
    }

    public List<BasicBlock> getBoundaryBlocksForLocalVariableOcurrence( GraphNode localVariableOcurrence){
        List<BasicBlock> boundaryBlock = new ArrayList<>();
        for(BasicBlock basicBlock : cfg.getBasicBlocks()){
            if(basicBlock.getDominatedBlocks().contains(localVariableOcurrence.getBasicBlock()) && basicBlock.getReachableBlocks().contains(localVariableOcurrence.getBasicBlock())){
                boundaryBlock.add(basicBlock);
            }
        }
        return boundaryBlock;
    }
    public Map<GraphNode, List<BasicBlock>> getAllBoundaryBlocksForCompleteComputation(){
        Map<GraphNode, List<BasicBlock>> boundaryBlocksForCompleteComputation = new HashMap<>();
        for(Map.Entry<GraphNode,List<GraphNode>> entry : localVariableAssigmentOcurrences.entrySet()/*getStatementsLocalVariableIsAssigned().entrySet()*/){
            //HashSet used to get a list without repetead basicblocks
            HashSet<BasicBlock> basicBlockSet = new LinkedHashSet<>();
            for(GraphNode localVariableAssignedOcurrence : entry.getValue()){
                basicBlockSet.addAll(getBoundaryBlocksForLocalVariableOcurrence( localVariableAssignedOcurrence));
            }
            boundaryBlocksForCompleteComputation.put(entry.getKey(), (ArrayList<BasicBlock>)basicBlockSet.stream()
                    .collect(Collectors.toList()));
        }
        return boundaryBlocksForCompleteComputation;
    }

    public Map<GraphNode, List<List<BasicBlock>>> getBoundaryBlocksForLocalAssigmentsInSeparetedLists(){
        Map<GraphNode, List<List<BasicBlock>>> returnedMap = new HashMap<>();
        for(Map.Entry<GraphNode, List<GraphNode>> entry : localVariableAssigmentOcurrences.entrySet()){
            List<List<BasicBlock>> intersection = new ArrayList<>();
            for(GraphNode node : entry.getValue()){
                intersection.add(getBoundaryBlocksForLocalVariableOcurrence(node));
            }
            returnedMap.put(entry.getKey(),intersection);
        }
        return returnedMap;
    }

    public Map<GraphNode, List<BasicBlock>> getIntersectionOfBoundaryBlocks(){
        Map<GraphNode, List<BasicBlock>> intersection = new HashMap<>();
        for(Map.Entry<GraphNode, List<List<BasicBlock>>> entry : getBoundaryBlocksForLocalAssigmentsInSeparetedLists().entrySet()){
            HashSet<BasicBlock> intersectionSet = new HashSet<>(entry.getValue().get(0));

            for (int i = 1; i < entry.getValue().size(); i++)
            {
                HashSet<BasicBlock> set = new HashSet<>(entry.getValue().get(i));

                intersectionSet.retainAll(set);
            }
            intersection.put(entry.getKey(), intersectionSet.stream().collect(Collectors.toList()));
        }
        return intersection;
    }

    public List<GraphNode> getAllLocalVariablesForThisMethod(){
        return allLocalVariablesForMethod;
    }
    public Map<GraphNode, List<GraphNode>> getLocalVariablesOcurrences(){
        return localVariablesOcurrences;
    }
    public Map<GraphNode, List<GraphNode>> getLocalVariableAssigmentOcurrences(){
        return localVariableAssigmentOcurrences;
    }
}
