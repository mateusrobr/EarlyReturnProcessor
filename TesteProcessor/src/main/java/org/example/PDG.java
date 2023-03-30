package org.example;

import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.reference.CtReference;
import spoon.reflect.visitor.chain.CtConsumer;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.reflect.visitor.filter.VariableReferenceFunction;
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
        //List<CtStatement> statementList = cfg.getCtStatements();

    }

    private List<CtLocalVariable> getAllCtLocalVariableFromMethod(CtMethod targetMethod){
        return targetMethod.getElements(new TypeFilter<>(CtLocalVariable.class));
    }
    public Map<GraphNode, List<CtReference>> getCtReferenceForAllOcurrencesOfAVariable(/*Map<GraphNode, CtStatement> graphNodeMap*/){
        Map<GraphNode, List<CtReference>> statementMap = new HashMap<>();
        List<List<CtReference>> ctReferenceList = new ArrayList<>();
        for(Map.Entry<GraphNode, CtStatement> entry : cfg.getMapGraphNodeCtStatement().entrySet()){
            //int counter = 0;
            ctReferenceList.add(new ArrayList<>());
            entry.getKey().getStatement().map(new VariableReferenceFunction()).forEach(new CtConsumer<CtReference>() {
                public void accept(CtReference t){
                    //ctReferenceList.add(new ArrayList<>());
                    ctReferenceList.get(ctReferenceList.size() - 1).add(t);
                }
            });

            statementMap.put(entry.getKey(), ctReferenceList.get(ctReferenceList.size() - 1));
        }
        
        return statementMap;
    }

    public List<GraphNode> getAllLocalVariablesForThisMethod(){
        return allLocalVariablesForMethod;
    }
}
