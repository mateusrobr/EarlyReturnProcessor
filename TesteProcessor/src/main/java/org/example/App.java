package org.example;

import spoon.Launcher;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.NamedElementFilter;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //String path = "/home/facomp/IdeaProjects/IfCounterRepo/IfCounter/src/main/java/org/example";
        String path = "C:\\Users\\Kurumi\\Desktop\\ERP\\EarlyReturnProcessor\\TesteProcessor\\src\\main\\java\\BaseMethodsForTesting";
        //String targetClassName = "Main";
        //String path = "/home/facomp/IdeaProjects/IfCounterRepo/IfCounter/src/main/java/org/example";
       // String targetClassName = "TesteIfs";


        Launcher launcher = new Launcher();
        launcher.addInputResource(path);
        launcher.buildModel();

        CtMethod method = (CtMethod) launcher.getModel().getElements(new NamedElementFilter(CtMethod.class, "teste01")).get(0);



        CFG cfg = new CFG(method);
        //cfg.printNodesFromBasicBlocks();

        System.out.println("Edges: ");

        for(GraphNode node : cfg.getAllNodes()){

            System.out.println(node);
            System.out.println("Edge: ");
            System.out.println(node.getOutgoingEdges());
            System.out.println(" ----------------");
        }

        cfg.printNodesFromBasicBlocks();
        for(BasicBlock basicBlock : cfg.getBasicBlocks()){
            System.out.println(basicBlock.getOutgoingEdges());
        }



    }
}
