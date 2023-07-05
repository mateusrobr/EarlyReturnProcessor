package org.example;

import spoon.reflect.code.*;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.path.CtRole;
import spoon.reflect.reference.CtReference;
import spoon.reflect.reference.CtVariableReference;
import spoon.reflect.visitor.chain.CtConsumer;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.reflect.visitor.filter.VariableReferenceFunction;
import spoon.support.reflect.code.*;

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

                    if(!(assignment.getAssigned() instanceof CtFieldWrite)){

                        CtReference reference = (CtReference) assignment.getAssigned().getDirectChildren().get(0);
                        if(localVariable.getSimpleName().equals(reference.getSimpleName())){
                            auxList.add(node);
                        }
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

    public void addDependencesToNodes(){

        List<CtReference> referenceList = new ArrayList<>();
        for( GraphNode node : cfg.getAllNodes()){
            System.out.println("Node: " + node);
            System.out.println("CtReferences: ");
            if(node.getStatement() instanceof CtIfImpl){
                CtIf branchNode = (CtIf) node.getStatement();
                System.out.println("References raw: ");
                System.out.println(branchNode.getCondition().filterChildren(new TypeFilter<>(CtVariableReference.class)).list());
                for(Object reference: branchNode.getCondition().filterChildren(new TypeFilter<>(CtVariableReference.class)).list()){
                    System.out.println(getCompleteVariable(node, (CtReference) reference));
                    System.out.println(getCompleteVariable(node, (CtReference) reference).getClass());
                }
            }
            else{
                System.out.println("References raw: ");
                System.out.println(node.getStatement().filterChildren(new TypeFilter<>(CtVariableReference.class)).list());
                for(Object reference: node.getStatement().filterChildren(new TypeFilter<>(CtVariableReference.class)).list()){
                    System.out.println(getCompleteVariable(node, (CtReference) reference));
                    System.out.println(getCompleteVariable(node, (CtReference) reference).getClass());
                }
            }
            //System.out.println(node.getStatement().getElements(new TypeFilter<>(CtStatement.class)));
        }
    }


    public CtElement getCompleteVariable(GraphNode parent, CtReference child){
        CtElement wantedStatement = child.getParent();
        while(wantedStatement.getParent() != parent.getStatement()){
            wantedStatement = wantedStatement.getParent();
        }
        return wantedStatement;
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
