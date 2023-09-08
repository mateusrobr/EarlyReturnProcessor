package org.example;

import spoon.reflect.code.*;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.reference.CtParameterReference;
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
    private Map<GraphNode, List<CtReference>> referencesAux;
    private Map<GraphNode, List<GraphNode>> localVariableAssigmentOcurrences;

    private Map<GraphNode,List<BasicBlock>> completeComputationBoundaryBlocks;

    private Map<GraphNode, List<GraphNode>> remaingNodesFromSlice;

    private List<CtParameter> parameterMethod;
    public PDG(CtMethod targetMethod){
        cfg = new CFG(targetMethod);
        allLocalVariablesForMethod = new ArrayList<>();
        parameterMethod = targetMethod.filterChildren(new TypeFilter<>(CtParameter.class)).list();
        this.fromGraphNodesGetLocalVariables();
        referencesAux = new LinkedHashMap<>();
        localVariablesOcurrences = getMapOfLocalVariableAndAllOcurrencesOfLocalVariable();
        localVariableAssigmentOcurrences = getStatementsLocalVariableIsAssigned();
        addDependencesToNodes();
        completeComputationBoundaryBlocks = getAllBoundaryBlocksForCompleteComputation();
        remaingNodesFromSlice = getRemaingNodesFromBoundaryBlocks();
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
//    public Map<GraphNode, List<GraphNode>> getMapOfParameterAndAllOcurrencesOfLocalVariable(){
//        Map<GraphNode, List<GraphNode>> statementMap = new HashMap<>();
//        List<List<GraphNode>> auxList = new ArrayList<>();
//        List<CtStatement> allCFGCtStatements = cfg.getAllCtStatements();
//        for(Map.Entry<GraphNode, CtStatement> entry : cfg.getMapGraphNodeCtStatement().entrySet()){
//            auxList.add(new ArrayList<>());
//            List<GraphNode> subList = new ArrayList<>();
//            List<CtReference> referenceList = new ArrayList<>();
//            entry.getKey().getStatement().map(new VariableReferenceFunction()).forEach(new CtConsumer<CtReference>() {
//                public void accept(CtReference t){
//                    auxList.get(auxList.size() - 1).add(getGraphNodeFromCtReference(t, allCFGCtStatements));
//                    referenceList.add(t);
//                }
//            });
//            referencesAux.put(entry.getKey(),referenceList);
//            statementMap.put(entry.getKey(), auxList.get(auxList.size() - 1));
//        }
//
//        return statementMap;
//    }
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
    public List<BasicBlock> getReachedBlocksForNode( GraphNode localVariableOcurrence){
        List<BasicBlock> reachableBlocks = new ArrayList<>();
        for(BasicBlock basicBlock : cfg.getBasicBlocks()){
            if(basicBlock.getReachableBlocks().contains(localVariableOcurrence.getBasicBlock())){
                reachableBlocks.add(basicBlock);
            }
        }
        return reachableBlocks;
    }
    public Map<GraphNode, List<BasicBlock>> getAllBoundaryBlocksForCompleteComputation(){
        Map<GraphNode, List<BasicBlock>> boundaryBlocksForCompleteComputation = new HashMap<>();
        for(Map.Entry<GraphNode,List<GraphNode>> entry : localVariableAssigmentOcurrences.entrySet()/*getStatementsLocalVariableIsAssigned().entrySet()*/){
            //HashSet used to get a list without repetead basicblocks
            HashSet<BasicBlock> basicBlockSet = new LinkedHashSet<>();
            for(GraphNode localVariableAssignedOcurrence : entry.getValue()){
                for (BasicBlock block : getBoundaryBlocksForLocalVariableOcurrence( localVariableAssignedOcurrence)){
                    if (block.getId() >= entry.getKey().getBasicBlock().getId()){
                        basicBlockSet.add(block);
                    }
                }
                //basicBlockSet.addAll(getBoundaryBlocksForLocalVariableOcurrence( localVariableAssignedOcurrence));
                for(GraphEdgeNode dataDependenceEdge : localVariableAssignedOcurrence.getDataDependenceLocalStatements()){
                    basicBlockSet.add(dataDependenceEdge.getDst().getBasicBlock());
                }
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
        for(GraphNode node : cfg.getAllNodes()){
            if(node.getStatement() instanceof CtIfImpl){
               CtIf branchNode = (CtIf) node.getStatement();;

                searchDependency(branchNode, node);
            }
            else{
                for (Object reference : node.getStatement().filterChildren(new TypeFilter<>(CtVariableReference.class)).list()){
                    CtReference reference1 = (CtReference) reference;
                    searchDependency(reference1, node);
                }
            }
        }
    }
    private void searchDependency(CtIf branchNode, GraphNode node){

        for(Object reference : branchNode.getCondition().filterChildren(new TypeFilter<>(CtVariableReference.class)).list()){
            if(reference instanceof CtParameterReference){
                CtParameterReference parameterReference = (CtParameterReference) reference;
                for(CtParameter methodParameter : parameterMethod){
                    if(methodParameter.getSimpleName().equals(parameterReference.getSimpleName())){

                        node.setDataDependenceParameters(methodParameter);
                        return; // If is a parameter then it's not needed to continue
                    }
                }
            }
            CtReference objectReference = (CtReference) reference;
            for(Map.Entry<GraphNode, List<CtReference>> entry : referencesAux.entrySet()){
                if(entry.getValue().contains(objectReference)){
                    //node.setDataDependenceLocalStatements(localVariablesOcurrences.get(entry.getKey()).get( localVariablesOcurrences.get(entry.getKey()).indexOf(getGraphNodeFromCtReference((CtReference) reference, cfg.getAllCtStatements())) -1 ));
                    List<GraphNode> assignmentsWithIdSmaller = getAssignmentNodesSmallerId(getGraphNodeFromCtReference(objectReference, cfg.getAllCtStatements()), entry.getKey());
                    for(int i = assignmentsWithIdSmaller.size() - 1; i >= 0 ; i-- ){
                        if(isReferenceInTheBoundaryBlocks(assignmentsWithIdSmaller.get(i), node)){
                            //node.setDependence(localVariablesOcurrences.get(entry.getKey()).get( index ));
                            node.setDataDependenceLocalStatements(assignmentsWithIdSmaller.get(i));
                            return;
                        }
                    }
                    //In case there's no assignment before this node
                    node.setDataDependenceLocalStatements(entry.getKey());
                }

            }
        }
    }
    private void searchDependency(CtReference reference, GraphNode node){
        //Verify if the reference for the dependency is a parameter
        if(reference instanceof CtParameterReference){
            CtParameterReference parameterReference = (CtParameterReference) reference;
            for(CtParameter methodParameter : parameterMethod){
                if(methodParameter.getSimpleName().equals(parameterReference.getSimpleName())){

                    node.setDataDependenceParameters(methodParameter);
                    return; // If is a parameter then it's not needed to continue
                }
            }
        }
        // if its not a parameter then it's necessary search through the local nodes.
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

                searchDataDependence(reference,entry.getKey(), node);

                if(node.getDataDependenceLocalStatements().size() == 0){
                    node.setDataDependenceLocalStatements(entry.getKey());
                    return;
                }
                else{
                    return;
                }
            }
        }
    }

    private void searchDataDependence(CtReference reference, GraphNode key, GraphNode node){
        List<GraphNode> assignmentsWithIdSmaller = getAssignmentNodesSmallerId(getGraphNodeFromCtReference(reference, cfg.getAllCtStatements()), key);
        List<GraphNode> visitedNodes = new ArrayList<>();
        for(int i = assignmentsWithIdSmaller.size() -1; i >= 0 ; i-- ){
            if(node.getBasicBlock() == assignmentsWithIdSmaller.get(i).getBasicBlock()){
                node.setDataDependenceLocalStatements(assignmentsWithIdSmaller.get(i));
                return;
            }
            else{
                transverseThroughGraph(assignmentsWithIdSmaller.get(i), node, assignmentsWithIdSmaller.get(i),visitedNodes, node.getDataDependenceLocalStatements().size());
            }
        }
    }
    public void transverseThroughGraph(GraphNode src, GraphNode target, GraphNode nodeToBeAddedToDependence,List<GraphNode> visitedNodes, int initialSizeDataDependenceList){
        GraphNode nodeAux;
        if(isReferenceInReachedBlocks(src,target) && !visitedNodes.contains(src)){
            //nodeAux = src;
            visitedNodes.add(src);
            for(GraphEdgeNode edge : src.getOutgoingEdges()){
                if(isReferenceInReachedBlocks(edge.getDst(), target)){
                    if(edge.getDst().getBasicBlock() == target.getBasicBlock()){
                        if(initialSizeDataDependenceList == target.getDataDependenceLocalStatements().size())
                            target.setDataDependenceLocalStatements(nodeToBeAddedToDependence);
                    }
                    else{
                        if(!visitedNodes.contains(edge.getDst())){
                            nodeAux = edge.getDst();
                            //System.out.println(nodeAux);
                            transverseThroughGraph(nodeAux,target,nodeToBeAddedToDependence,visitedNodes, initialSizeDataDependenceList);
                        }
                    }
                }
            }
        }
    }
    private boolean isReferenceInTheBoundaryBlocks(CtReference reference, GraphNode node){
        List<BasicBlock> boundaryBlockNode = getBoundaryBlocksForLocalVariableOcurrence(node);
        GraphNode referenceGraphNode = getGraphNodeFromCtReference(reference, cfg.getAllCtStatements());
        return boundaryBlockNode.contains(referenceGraphNode.getBasicBlock());
    }
    private boolean isReferenceInTheBoundaryBlocks(GraphNode nodeTested, GraphNode node){
        List<BasicBlock> boundaryBlockNode = getReachedBlocksForNode(node);
        return boundaryBlockNode.contains(nodeTested.getBasicBlock());
    }
    private boolean isReferenceInReachedBlocks(GraphNode nodeTested, GraphNode node){
        List<BasicBlock> boundaryBlockNode = getReachedBlocksForNode(node);
        return boundaryBlockNode.contains(nodeTested.getBasicBlock());
    }
    private List<GraphNode> getAssignmentNodesSmallerId(GraphNode node, GraphNode key){
        List<GraphNode> assignNodesWithSmallerId = new ArrayList<>();
        for (GraphNode assigNode : localVariableAssigmentOcurrences.get(key)){
            if(node.getId() > assigNode.getId()){
               assignNodesWithSmallerId.add(assigNode);
            }
            else{
                //If the Id is bigger so doesnt make sense continue because this dependence is for nodes that preceed this current node.
                break;
            }
        }
        return assignNodesWithSmallerId;
    }
    private List<GraphNode> getAssignmentNodesBiggerId(GraphNode node, GraphNode key){
        List<GraphNode> assignNodesWithSmallerId = new ArrayList<>();
        for (GraphNode assigNode : localVariableAssigmentOcurrences.get(key)){
            if(node.getId() < assigNode.getId()){
                assignNodesWithSmallerId.add(assigNode);
            }
            else{
                //If the Id is bigger so doesnt make sense continue because this dependence is for nodes that preceed this current node.
                break;
            }
        }
        return assignNodesWithSmallerId;
    }

    private Map<GraphNode,List<GraphNode>> getRemaingNodesFromBoundaryBlocks(){
        Map<GraphNode,List<GraphNode>> remainingNodes = new LinkedHashMap<>();
        Map<GraphNode, List<GraphNode>> statementsThatArePartOfCompleteCOmputation = new LinkedHashMap<>();
        Map<GraphNode, List<GraphNode>> statementsThatAreNotPartOfCompleteComputation = new LinkedHashMap<>();
        for(Map.Entry<GraphNode, List<BasicBlock>> entry : completeComputationBoundaryBlocks.entrySet()){
            List<GraphNode> nodes = new ArrayList<>();
            for(BasicBlock block : entry.getValue()){
                nodes.addAll(block.getNodes());
            }
            statementsThatArePartOfCompleteCOmputation.put(entry.getKey(), nodes);
            remainingNodes.put(entry.getKey(),new ArrayList<>());
        }
        for(Map.Entry<GraphNode, List<GraphNode>> entry : statementsThatArePartOfCompleteCOmputation.entrySet()){
            List<GraphNode> nodes = new ArrayList<>();
            for(GraphNode node : cfg.getAllNodes()){
                if(!entry.getValue().contains(node)){
                    nodes.add(node);
                }
            }
            statementsThatAreNotPartOfCompleteComputation.put(entry.getKey(), nodes);
        }
        for(Map.Entry<GraphNode, List<GraphNode>> entry: statementsThatArePartOfCompleteCOmputation.entrySet()){
            //System.out.println(entry.getKey());
            List<GraphNode> listAux = new ArrayList<>();
            for (GraphNode node : statementsThatArePartOfCompleteCOmputation.get(entry.getKey())){
                for(GraphEdgeNode edge : node.getOutgoingEdges()){
                    if(edge.getIsControlEdge()){
                        if(statementsThatAreNotPartOfCompleteComputation.get(entry.getKey()).contains(edge.getDst())){
                            listAux.add(node);
                        }
                    }
                    else{
                        if(edge.getSrc().getBasicBlock() != edge.getDst().getBasicBlock()){
                            if(edge.getDst().getBasicBlock().getControlDependent() == edge.getSrc().getBasicBlock()){
                                if(statementsThatAreNotPartOfCompleteComputation.get(entry.getKey()).contains(edge.getDst())){
                                    listAux.add(node.getBasicBlock().getControlDependent().getNodes().get( node.getBasicBlock().getControlDependent().getNodes().size() - 1 ));
                                }
                            }
                        }
                    }
                }
            }
            remainingNodes.get(entry.getKey()).addAll(listAux);
        }

        for(Map.Entry<GraphNode, List<GraphNode>> entry: statementsThatAreNotPartOfCompleteComputation.entrySet()){
            //System.out.println(entry.getKey());
            List<GraphNode> listAux = new ArrayList<>();
            for (GraphNode node : statementsThatAreNotPartOfCompleteComputation.get(entry.getKey())){
                for(GraphEdgeNode dataEdge : node.getDataDependenceLocalStatements() ){
                    if(statementsThatAreNotPartOfCompleteComputation.get(entry.getKey()).contains(dataEdge.getSrc()) && statementsThatArePartOfCompleteCOmputation.get(entry.getKey()).contains(dataEdge.getDst())){
                        if(!remainingNodes.get(entry.getKey()).contains(dataEdge.getDst())){
                            if(!localVariableAssigmentOcurrences.get(entry.getKey()).contains(dataEdge.getDst())){
                                //System.out.println("Data dependence de " + dataEdge.getSrc() + " Para " + dataEdge.getDst());
                                listAux.add(dataEdge.getDst());
                            }

                        }
                    }
                }
            }
            remainingNodes.get(entry.getKey()).addAll(listAux);
        }

//        for(Map.Entry<GraphNode, List<GraphNode>> entry: statementsThatAreNotPartOfCompleteComputation.entrySet()){
//            //System.out.println(entry.getKey());
//            List<GraphNode> listAux2 = new ArrayList<>();
//            for (GraphNode node : statementsThatAreNotPartOfCompleteComputation.get(entry.getKey())){
//                for(GraphEdgeNode edge : node.getDataDependenceLocalStatements()){
//                    if(statementsThatArePartOfCompleteCOmputation.get(entry.getKey()).contains(edge.getDst()) && localVariableAssigmentOcurrences.get(entry.getKey()).contains(edge.getDst())){
//                        listAux2.add(edge.getDst());
//
//                    }
//                }
//            }
//            remainingNodes.get(entry.getKey()).addAll(listAux2);
//        }

        return remainingNodes;
    }
    public Map<GraphNode,List<GraphNode>> getRemaingNodes(){
        return remaingNodesFromSlice;
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
    public List<CtParameter> getParametersMethod(){
        return this.parameterMethod;
    }
    public CFG getCfg(){
        return this.cfg;
    }
}
