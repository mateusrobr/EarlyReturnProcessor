package org.example;

import spoon.Launcher;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.NamedElementFilter;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.reflect.code.CtBlockImpl;
import spoon.support.reflect.code.CtIfImpl;
import spoon.support.reflect.code.CtSwitchImpl;
import spoon.support.reflect.code.CtWhileImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //String path = "F:\\Bolsa\\TesteIFS\\src";
        //String targetClassName = "Main";
        String path = "/home/facomp/IdeaProjects/IfCounterRepo/IfCounter/src/main/java/org/example";
        String targetClassName = "TesteIfs";


        Launcher launcher = new Launcher();
        launcher.addInputResource(path);
        launcher.buildModel();
        CtMethod method = (CtMethod) launcher.getModel().getElements(new NamedElementFilter(CtMethod.class, "teste6")).get(0);

        List<BasicBlock> basicBlockList = new ArrayList<>();
        Set<CtStatement> statementsFromMethod = new HashSet<>();
        for(CtStatement statement : method.getBody().getElements(new TypeFilter<>(CtStatement.class))){
            if(statement.getParent() == method.getBody()){
                statementsFromMethod.add(statement);
            }
        }
        int blockNumber = 1;
        basicBlockList.add(new BasicBlock());
        List<CtStatement> statementsToAddToBlock = new ArrayList<>();
        for(CtStatement statement : statementsFromMethod){
            if(statement instanceof CtIfImpl){
                basicBlockList.get(blockNumber).addNode(statementsToAddToBlock);
                statementsToAddToBlock.clear();
                basicBlockList.add(new BasicBlock());
                basicBlockList.get(blockNumber).setOutgoingEdges(new GraphEdge());
                continue;
            }
            if(statement instanceof CtSwitchImpl){
                continue;
            }
            if(statement instanceof CtWhileImpl){
                continue;
            }
            if(statement instanceof CtFor){
                continue;
            }
            statementsToAddToBlock.add(statement);
        }

        System.out.println(statementsToAddToBlock);






    }
}
