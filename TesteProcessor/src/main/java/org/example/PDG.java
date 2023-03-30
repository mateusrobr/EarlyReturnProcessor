package org.example;

import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtReference;
import spoon.reflect.visitor.chain.CtConsumer;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.reflect.visitor.filter.VariableReferenceFunction;
import spoon.support.reflect.code.CtBlockImpl;
import spoon.support.reflect.code.CtLocalVariableImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PDG {
    private CFG cfg;
    private List<GraphNode> allLocalVariablesForMethod;

    private Map<GraphNode, List<GraphNode>> localVariablesOcurrences;
    public PDG(CtMethod targetMethod){
        cfg = new CFG(targetMethod);
        allLocalVariablesForMethod = new ArrayList<>();
        this.fromGraphNodesGetLocalVariables();
    }

    private void fromGraphNodesGetLocalVariables(){
        for(GraphNode node : cfg.getAllNodes()){
            if (node.getStatement().getClass() == CtLocalVariableImpl.class){
                allLocalVariablesForMethod.add(node);
            }
        }
    }
    public void fromCFGGraphNodesGetAllOcurrencesOfALocalVariable(){
        getCtStatementForAllOcurrencesOfAVariable();

    }

    private List<CtLocalVariable> getAllCtLocalVariableFromMethod(CtMethod targetMethod){
        return targetMethod.getElements(new TypeFilter<>(CtLocalVariable.class));
    }
    public Map<GraphNode, List<CtStatement>> getCtStatementForAllOcurrencesOfAVariable(){
        Map<GraphNode, List<CtStatement>> statementMap = new HashMap<>();
        List<List<CtStatement>> listWithListOfCtReference = new ArrayList<>();
        for(Map.Entry<GraphNode, CtStatement> entry : cfg.getMapGraphNodeCtStatement().entrySet()){
            listWithListOfCtReference.add(new ArrayList<>());
            entry.getKey().getStatement().map(new VariableReferenceFunction()).forEach(new CtConsumer<CtReference>() {
                public void accept(CtReference t){

                    listWithListOfCtReference.get(listWithListOfCtReference.size() - 1).add(getCompleteStatementFromVariableReference(t));
                }
            });

            statementMap.put(entry.getKey(), listWithListOfCtReference.get(listWithListOfCtReference.size() - 1));
        }

        return statementMap;
    }
    public CtStatement getCompleteStatementFromVariableReference(CtReference reference){
        CtElement completeStatement =  reference.getParent();

        while(completeStatement.getParent().getClass() != CtBlockImpl.class){
            completeStatement = completeStatement.getParent();
        }
        return (CtStatement) completeStatement;
    }

    public List<GraphNode> getAllLocalVariablesForThisMethod(){
        return allLocalVariablesForMethod;
    }
}
