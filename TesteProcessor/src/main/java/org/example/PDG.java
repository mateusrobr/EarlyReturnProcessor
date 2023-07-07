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

import java.lang.ref.Reference;
import java.util.*;
import java.util.stream.Collectors;

public class PDG {
    private CFG cfg;
    private List<GraphNode> allLocalVariablesForMethod;

    private Map<GraphNode, List<GraphNode>> localVariablesOcurrences;
    private Map<GraphNode, List<CtReference>> referencesAux;

    private Map<GraphNode, List<GraphNode>> localVariableAssigmentOcurrences;
    public PDG(CtMethod targetMethod){
        cfg = new CFG(targetMethod);
        allLocalVariablesForMethod = new ArrayList<>();
        this.fromGraphNodesGetLocalVariables();
        referencesAux = new LinkedHashMap<>();
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
            List<GraphNode> subList = new ArrayList<>();
            List<CtReference> referenceList = new ArrayList<>();
            entry.getKey().getStatement().map(new VariableReferenceFunction()).forEach(new CtConsumer<CtReference>() {
                public void accept(CtReference t){
                    auxList.get(auxList.size() - 1).add(getGraphNodeFromCtReference(t, allCFGCtStatements));
                    referenceList.add(t);
                }
            });
            referencesAux.put(entry.getKey(),referenceList);
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
        int correctIndex = 0;
        for(CtStatement statement : statementList){
            if(statement.getPosition() == completeStatement.getPosition()){
                return cfg.getAllNodes().get(correctIndex);
            }
            correctIndex++;
        }
        //Not used
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
        //System.out.println(localVariablesOcurrences);
        for(GraphNode node : cfg.getAllNodes()){
            //System.out.println(node);
            if(node.getStatement() instanceof CtIfImpl){
               CtIf branchNode = (CtIf) node.getStatement();;
//                for(Object reference : branchNode.getCondition().filterChildren(new TypeFilter<>(CtVariableReference.class)).list()){
//                    for(Map.Entry<GraphNode, List<CtReference>> entry : referencesAux.entrySet()){
//                        if(entry.getValue().contains(reference)){
//                            node.setDependence(localVariablesOcurrences.get(entry.getKey()).get( localVariablesOcurrences.get(entry.getKey()).indexOf(getGraphNodeFromCtReference((CtReference) reference, cfg.getAllCtStatements())) -1 ));
//                        }
//                    }
//                }
                searchDependency(branchNode, node);
            }
            else{
                for (Object reference : node.getStatement().filterChildren(new TypeFilter<>(CtVariableReference.class)).list()){
                    //System.out.println("reference: " + reference);
                    CtReference reference1 = (CtReference) reference;
                    searchDependency(reference1, node);
//                    for(Map.Entry<GraphNode, List<CtReference>> entry : referencesAux.entrySet()){
//                        //System.out.println(entry.getValue().contains(reference));
//                        if(entry.getValue().contains(reference)){
//                            if(node.getStatement() instanceof CtAssignmentImpl){
//                                CtAssignment assignment = (CtAssignment) node.getStatement();
//                                CtReference referenceTest = (CtReference) assignment.getAssigned().getDirectChildren().get(0);
//                                if(referenceTest.getSimpleName().equals(reference1.getSimpleName())){
//                                    continue;
//                                }
//                            }
//                            int index = localVariablesOcurrences.get(entry.getKey()).indexOf(getGraphNodeFromCtReference((CtReference) reference, cfg.getAllCtStatements()));
//                            //System.out.println(index);
//                            if(index == 0){
//                                node.setDependence(entry.getKey());
//                                continue;
//                            }
//                            node.setDependence(localVariablesOcurrences.get(entry.getKey()).get( index - 1));
//                        }
//                    }
                }
            }
        }
    }
    private void searchDependency(CtIf branchNode, GraphNode node){

        for(Object reference : branchNode.getCondition().filterChildren(new TypeFilter<>(CtVariableReference.class)).list()){
            for(Map.Entry<GraphNode, List<CtReference>> entry : referencesAux.entrySet()){
                if(entry.getValue().contains(reference)){
                    node.setDependence(localVariablesOcurrences.get(entry.getKey()).get( localVariablesOcurrences.get(entry.getKey()).indexOf(getGraphNodeFromCtReference((CtReference) reference, cfg.getAllCtStatements())) -1 ));
                }
            }
        }
    }
    private void searchDependency(CtReference reference, GraphNode node){
        for(Map.Entry<GraphNode, List<CtReference>> entry : referencesAux.entrySet()){
            //System.out.println(entry.getValue().contains(reference));
            if(entry.getValue().contains(reference)){
                if(node.getStatement() instanceof CtAssignmentImpl){
                    CtAssignment assignment = (CtAssignment) node.getStatement();
                    CtReference referenceTest = (CtReference) assignment.getAssigned().getDirectChildren().get(0);
                    if(referenceTest.getSimpleName().equals(reference.getSimpleName())){
                        continue;
                    }
                }
                int index = localVariablesOcurrences.get(entry.getKey()).indexOf(getGraphNodeFromCtReference((CtReference) reference, cfg.getAllCtStatements()));
                //System.out.println(index);
                if(index == 0){
                    node.setDependence(entry.getKey());
                    continue;
                }
                node.setDependence(localVariablesOcurrences.get(entry.getKey()).get( index - 1));
            }
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
    public CFG getCfg(){
        return this.cfg;
    }
}
