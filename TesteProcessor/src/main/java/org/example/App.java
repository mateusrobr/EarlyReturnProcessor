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


        /*Launcher launcher = new Launcher();
        launcher.addInputResource(path);
        CtModel model = launcher.buildModel();
        EarlyReturnProcessor earlyReturnProcessor = new EarlyReturnProcessor();


        CtClass targetClassSpoon = (CtClass) model.getElements(new NamedElementFilter(CtClass.class, "Main")).get(0);


        System.out.println(targetClassSpoon.prettyprint());
        for (CtMethod method : targetClassSpoon.getElements(new TypeFilter<>(CtMethod.class))){

            if(earlyReturnProcessor.isToBeProcessed(method)){
                earlyReturnProcessor.process(method);
            }

        }
        System.out.println(targetClassSpoon.prettyprint());*/

        //Teste getPackages

        Launcher launcher = new Launcher();
        launcher.addInputResource(path);
        launcher.buildModel();
        CtMethod method = (CtMethod) launcher.getModel().getElements(new NamedElementFilter(CtMethod.class, "teste6")).get(0);

        List<CtStatement> list = new ArrayList<>();
        for(CtStatement statement : method.getElements(new TypeFilter<>(CtStatement.class))){
            if(statement.getParent() == method.getBody()){
                list.add(statement);
            }
        }
        //System.out.println(list);

        List<GraphNode> nodeList = new ArrayList<>();
        List<GraphEdge> edgeList = new ArrayList<>();
        /*GraphNode node = new GraphNode(list.get(0));
        GraphEdge edge = new GraphEdge();
        edge.setDst(new GraphNode(list.get(1)));
        edge.setSrc(node);
        node.setOutgoingEdges(edge);

        System.out.println(node.getIncomingEdges());*/


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
            /*if(i == 0){
                nodeList.get(i).setIncomingEdge(new GraphEdge(null, null));
                i++;
                continue;
            }
            else if (nodeList.size() - 1 == i){
                nodeList.get(i).setIncomingEdge(new GraphEdge(nodeList.get(i), nodeList.get(i - 1)));
                break;
            }*/
            nodeList.get(i).setIncomingEdge(new GraphEdge(nodeList.get(i), nodeList.get(i - 1)));
            i++;
        }

        System.out.println(nodeList.get(4).getOutgoingEdges());
        System.out.println(nodeList.get(4).getIncomingEdges());




    }
}
