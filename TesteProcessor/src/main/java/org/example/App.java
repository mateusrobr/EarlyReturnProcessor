package org.example;

import spoon.Launcher;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.NamedElementFilter;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.ArrayList;
import java.util.List;

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

        /*List<CtStatement> list = new ArrayList<>();
        for(CtStatement statement : method.getElements(new TypeFilter<>(CtStatement.class))){
            if(statement.getParent() == method.getBody()){
                list.add(statement);
            }
        }


        List<GraphNode> nodeList = new ArrayList<>();
        for(CtStatement statement : list){
            nodeList.add(new GraphNode(statement));
        }
        int i = 0;
        while(nodeList.size() > i){
            if(nodeList.size() - 1 == i ){
                nodeList.get(i).setOutgoingEdges(new GraphEdge(nodeList.get(i), null));
                break;
            }
            nodeList.get(i).setOutgoingEdges(new GraphEdge(nodeList.get(i), nodeList.get(i + 1)));
            i++;
        }
        i = 1;
        while(nodeList.size() > i){
            nodeList.get(i).setIncomingEdge(new GraphEdge(nodeList.get(i), nodeList.get(i - 1)));
            i++;
        }

        System.out.println(nodeList.get(4).getOutgoingEdges());
        System.out.println(nodeList.get(4).getIncomingEdges());*/

        BasicBlock blockTeste = new BasicBlock();

        blockTeste.addNodesFromCtBlock(method.getBody());
        blockTeste.printNodes();





    }
}
